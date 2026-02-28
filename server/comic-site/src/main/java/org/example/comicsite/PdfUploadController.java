package org.example.comicsite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class PdfUploadController {

    @PostMapping("/upload-new-pdf")  // Теперь этот метод обрабатывает другой путь /api/upload-new-pdf
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        // Логика обработки нового PDF
        return ResponseEntity.ok("Новый PDF загружен!");
    }
}