package com.example.comics.controller;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ComicUploadController {

    private final String UPLOAD_DIR = "uploads"; // папка для хранения cbr и картинок

    @PostMapping("/upload-cbr")
    public ResponseEntity<?> uploadAndExtractCbr(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.endsWith(".cbr")) {
            return ResponseEntity.badRequest().body("Только .cbr файлы разрешены");
        }

        // Создаем папку для загрузки
        Path uploadPath = Paths.get(UPLOAD_DIR, filename);
        Files.createDirectories(uploadPath.getParent());
        file.transferTo(uploadPath);

        // Папка для распакованных страниц
        String baseName = filename.substring(0, filename.lastIndexOf("."));
        Path extractDir = Paths.get(UPLOAD_DIR, baseName);
        Files.createDirectories(extractDir);

        try (Archive archive = new Archive(uploadPath.toFile())) {
            List<String> pagePaths = new ArrayList<>();
            for (FileHeader fh : archive.getFileHeaders()) {
                if (!fh.isDirectory()) {
                    File out = extractDir.resolve(fh.getFileNameString().trim()).toFile();
                    // создаем родительскую папку, если нужна
                    out.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(out)) {
                        archive.extractFile(fh, fos);
                    }
                    // сохраняем относительный путь для фронтенда
                    pagePaths.add("/uploads/" + baseName + "/" + fh.getFileNameString().trim());
                }
            }
            // сортируем страницы по имени
            pagePaths.sort(String::compareTo);
            return ResponseEntity.ok(Map.of("pages", pagePaths));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ошибка распаковки CBR: " + e.getMessage());
        }
    }
}