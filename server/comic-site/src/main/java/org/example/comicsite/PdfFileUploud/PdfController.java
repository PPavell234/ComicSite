package org.example.comicsite.PdfFileUploud;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequestMapping("/api")
public class PdfController {

    private static final Logger logger = LoggerFactory.getLogger(PdfController.class);
    private static final String UPLOAD_DIR = "uploads";
    private static final int DPI = 150;

    //Очищяем фалы
    @DeleteMapping("/temp-file/{filename}")
    public ResponseEntity<?> deleteTempFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            Files.deleteIfExists(filePath);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            logger.info("Обработка PDF файла: {}", file.getOriginalFilename());

            // Загружаем PDF в память
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            int numberOfPages = document.getNumberOfPages();
            List<String> pagesBase64 = new ArrayList<>();

            // Конвертируем страницы в Base64 (без сохранения на диск)
            for (int page = 0; page < numberOfPages; page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 150);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                byte[] imageBytes = baos.toByteArray();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                pagesBase64.add("data:image/png;base64," + base64Image);

                logger.debug("Страница {} сконвертирована", page + 1);
            }

            document.close();

            response.put("message", "Файл успешно обработан");
            response.put("totalPages", numberOfPages);
            response.put("pages", pagesBase64);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            logger.error("Ошибка при обработке PDF: {}", e.getMessage(), e);
            response.put("error", "Ошибка при обработке PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Файл слишком большой! Максимальный размер: 50MB");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }
}