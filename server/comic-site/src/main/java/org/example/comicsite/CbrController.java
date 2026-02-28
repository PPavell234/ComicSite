package org.example.comicsite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/api")
public class CbrController {

    @PostMapping("/upload-cbr")
    public ResponseEntity<?> uploadCbr(@RequestParam("file") MultipartFile file) {
        List<String> pages = new ArrayList<>();
        File tempFile = null;
        File tempZip = null;

        try {
            System.out.println("Получен файл: " + file.getOriginalFilename() + ", размер: " + file.getSize());

            // Сохраняем CBR во временный файл
            tempFile = File.createTempFile("upload-", ".cbr");
            file.transferTo(tempFile);

            // Преобразуем CBR (RAR) в CBZ (ZIP) с помощью 7z (нужен установленный 7-Zip или p7zip)
            tempZip = File.createTempFile("upload-", ".cbz");
            convertCbrToCbz(tempFile, tempZip);

            // Распаковываем CBZ
            try (ZipInputStream zis = new ZipInputStream(new FileInputStream(tempZip))) {
                ZipEntry entry;
                List<ZipEntry> imageEntries = new ArrayList<>();

                // Собираем только изображения и сортируем по имени
                while ((entry = zis.getNextEntry()) != null) {
                    String name = entry.getName().toLowerCase();
                    if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")) {
                        imageEntries.add(new ZipEntry(name));
                    }
                }

                // Снова читаем ZIP для Base64
                try (ZipInputStream zis2 = new ZipInputStream(new FileInputStream(tempZip))) {
                    while ((entry = zis2.getNextEntry()) != null) {
                        String name = entry.getName().toLowerCase();
                        if (!name.endsWith(".jpg") && !name.endsWith(".jpeg") && !name.endsWith(".png")) continue;

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = zis2.read(buffer)) > 0) {
                            baos.write(buffer, 0, len);
                        }

                        String base64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
                        pages.add(base64);
                    }
                }
            }

            if (pages.isEmpty()) {
                return ResponseEntity.status(500).body("Нет изображений в архиве");
            }

            return ResponseEntity.ok().body(new PagesResponse(pages));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body("Ошибка обработки CBR/CBZ: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        } finally {
            if (tempFile != null) tempFile.delete();
            if (tempZip != null) tempZip.delete();
        }
    }

    // Временный конвертер CBR -> CBZ через системный 7z
    private void convertCbrToCbz(File cbr, File cbz) throws IOException, InterruptedException {
        String command = String.format("7z x \"%s\" -o\"%s\" -y", cbr.getAbsolutePath(), cbz.getParent());
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();

        // Создаём CBZ (ZIP) из распакованных файлов
        try (FileOutputStream fos = new FileOutputStream(cbz);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(bos)) {

            File[] files = new File(cbz.getParent()).listFiles();
            if (files == null) return;

            for (File f : files) {
                if (f.isFile() && (f.getName().endsWith(".jpg") || f.getName().endsWith(".png") || f.getName().endsWith(".jpeg"))) {
                    zos.putNextEntry(new ZipEntry(f.getName()));
                    Files.copy(f.toPath(), zos);
                    zos.closeEntry();
                    f.delete(); // удаляем временный файл
                }
            }
        }
    }

    static class PagesResponse {
        public List<String> pages;
        public PagesResponse(List<String> pages) { this.pages = pages; }
    }
}