package org.example.comicsite;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Указываем временную папку для хранения файла
            String tempDirPath = "/path/to/temp-folder";  // Измените на путь к вашей временной папке
            File tempDir = new File(tempDirPath);
            if (!tempDir.exists()) {
                tempDir.mkdirs();  // Создаем папку, если её нет
            }

            // Сохраняем файл во временную папку
            File tempFile = new File(tempDir, file.getOriginalFilename());
            file.transferTo(tempFile);

            return ResponseEntity.ok("Файл успешно загружен в: " + tempFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ошибка загрузки файла: " + e.getMessage());
        }
    }
}