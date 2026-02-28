package org.example.comicsite;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
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

    @PostMapping("/upload-pdf")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("error", "Файл не выбран");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
            response.put("error", "Файл должен быть в формате PDF");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Path uploadPath = Paths.get(UPLOAD_DIR);
        PDDocument document = null;

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            logger.info("Обработка PDF файла: {}", originalFilename);

            document = PDDocument.load(file.getInputStream());

            int numberOfPages = document.getNumberOfPages();

            if (numberOfPages == 0) {
                response.put("error", "PDF файл не содержит страниц");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Сохраняем файл
            String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Конвертируем все страницы в Base64
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            List<String> pagesBase64 = new ArrayList<>();

            for (int page = 0; page < numberOfPages; page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, DPI);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                byte[] imageBytes = baos.toByteArray();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                pagesBase64.add("data:image/png;base64," + base64Image);

                logger.debug("Страница {} сконвертирована", page + 1);
            }

            response.put("message", "Файл успешно обработан");
            response.put("filename", uniqueFilename);
            response.put("totalPages", numberOfPages);
            response.put("pages", pagesBase64); // Возвращаем массив страниц

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            logger.error("Ошибка при обработке PDF: {}", e.getMessage(), e);
            response.put("error", "Ошибка при обработке PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    logger.error("Ошибка при закрытии документа: {}", e.getMessage());
                }
            }
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Файл слишком большой! Максимальный размер: 50MB");
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }
}