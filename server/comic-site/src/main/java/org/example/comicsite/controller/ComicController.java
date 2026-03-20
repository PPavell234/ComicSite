package org.example.comicsite.controller;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.bson.types.ObjectId;
import org.example.comicsite.model.Comic;
import org.example.comicsite.repository.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/comics")
@CrossOrigin(origins = "http://localhost:5173") // Важно для CORS!
public class ComicController {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoDatabaseFactory mongoDatabaseFactory;

    // ПОЛУЧИТЬ КОМИКС ПО ID
    @GetMapping("/{id}")
    public ResponseEntity<Comic> getComicById(@PathVariable String id) {
        System.out.println("Запрос комикса с ID: " + id);

        return comicRepository.findById(id)
                .map(comic -> {
                    System.out.println("Комикс найден: " + comic.getTitle());
                    return ResponseEntity.ok(comic);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    // Быстаря загрузка комикса
    @PostMapping("/upload-pdf")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            List<String> pageIds = new ArrayList<>();

            // Конвертируем каждую страницу и сохраняем в GridFS
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 150);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", baos);

                // Сохраняем страницу в GridFS
                ObjectId pageId = gridFsTemplate.store(
                        new ByteArrayInputStream(baos.toByteArray()),
                        "page_" + page + ".jpg",
                        "image/jpeg"
                );
                pageIds.add(pageId.toString());
            }

            document.close();

            Map<String, Object> response = new HashMap<>();
            response.put("pageIds", pageIds);
            response.put("totalPages", pageIds.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        }
    }

    // СОЗДАНИЕ КОМИКСА
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createComic(
            @RequestParam("userId") String userId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tags") String tags,
            @RequestParam("chapterNumber") Integer chapterNumber,
            @RequestParam("year") Integer year,
            @RequestParam("translator") String translator,
            @RequestParam("artist") String artist,
            @RequestParam("cover") MultipartFile coverFile,
            @RequestParam("pdf") MultipartFile pdfFile) {

        try {
            System.out.println("=== СОЗДАНИЕ КОМИКСА ===");
            System.out.println("Title: " + title);
            System.out.println("Chapter: " + chapterNumber);
            System.out.println("Cover file: " + coverFile.getOriginalFilename());
            System.out.println("Cover size: " + coverFile.getSize());
            System.out.println("PDF file: " + pdfFile.getOriginalFilename());

            // 1. Сохраняем обложку в GridFS
            ObjectId coverId = gridFsTemplate.store(
                    coverFile.getInputStream(),
                    coverFile.getOriginalFilename(),
                    coverFile.getContentType()
            );
            System.out.println("Cover saved with ID: " + coverId.toString());

            // 2. Сохраняем PDF в GridFS
            ObjectId pdfId = gridFsTemplate.store(
                    pdfFile.getInputStream(),
                    pdfFile.getOriginalFilename(),
                    pdfFile.getContentType()
            );
            System.out.println("PDF saved with ID: " + pdfId.toString());

            // 3. Создаем комикс
            Comic comic = new Comic();
            comic.setUserId(userId);
            comic.setTitle(title);
            comic.setDescription(description);

            List<String> tagList = Arrays.asList(tags.split(","));
            comic.setTags(tagList);

            comic.setChapterNumber(chapterNumber);
            comic.setYear(year);
            comic.setTranslator(translator);
            comic.setArtist(artist);
            comic.setCoverImageId(coverId.toString());
            comic.setPdfFileId(pdfId.toString());

            // 4. Сохраняем в БД
            Comic savedComic = comicRepository.save(comic);
            System.out.println("Comic saved with ID: " + savedComic.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Комикс успешно создан!");
            response.put("id", savedComic.getId());
            response.put("title", savedComic.getTitle());
            response.put("coverImageId", coverId.toString());
            response.put("pdfFileId", pdfId.toString());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("ОШИБКА: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка при создании комикса: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ПОЛУЧИТЬ ВСЕ КОМИКСЫ
    @GetMapping("/all")
    public ResponseEntity<List<Comic>> getAllComics() {
        try {
            List<Comic> comics = comicRepository.findAll();
            System.out.println("Найдено комиксов: " + comics.size());
            return ResponseEntity.ok(comics);
        } catch (Exception e) {
            System.err.println("Ошибка получения комиксов: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ПОЛУЧИТЬ ФАЙЛ ПО ID (ОБЛОЖКА)
    @GetMapping("/files/{fileId}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileId) {
        try {
            System.out.println("Запрос файла с ID: " + fileId);

            // Проверяем валидность ObjectId
            ObjectId objectId;
            try {
                objectId = new ObjectId(fileId);
            } catch (IllegalArgumentException e) {
                System.out.println("Невалидный ObjectId: " + fileId);
                return ResponseEntity.badRequest().build();
            }

            // Находим файл в GridFS
            var file = gridFsTemplate.findOne(
                    Query.query(Criteria.where("_id").is(objectId))
            );

            if (file == null) {
                System.out.println("Файл не найден в GridFS: " + fileId);
                return ResponseEntity.notFound().build();
            }

            System.out.println("Файл найден: " + file.getFilename());
            System.out.println("Размер файла: " + file.getLength());

            // Получаем GridFSBucket
            GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDatabaseFactory.getMongoDatabase());

            // Получаем InputStream и читаем байты
            byte[] bytes = IOUtils.toByteArray(gridFSBucket.openDownloadStream(objectId));

            HttpHeaders headers = new HttpHeaders();

            // Определяем Content-Type
            String contentType = "image/jpeg"; // по умолчанию
            if (file.getMetadata() != null && file.getMetadata().get("_contentType") != null) {
                contentType = file.getMetadata().getString("_contentType");
            }
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(bytes.length);

            // Добавляем заголовки для кэширования
            headers.setCacheControl("max-age=3600");

            System.out.println("Отправка файла: " + bytes.length + " байт, тип: " + contentType);

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("Ошибка при получении файла: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ТЕСТОВЫЙ ENDPOINT ДЛЯ ПРОВЕРКИ ФАЙЛА
    @GetMapping("/test-file/{fileId}")
    public ResponseEntity<Map<String, Object>> testFile(@PathVariable String fileId) {
        Map<String, Object> result = new HashMap<>();
        try {
            ObjectId objectId = new ObjectId(fileId);

            var file = gridFsTemplate.findOne(
                    Query.query(Criteria.where("_id").is(objectId))
            );

            if (file == null) {
                result.put("found", false);
                result.put("message", "Файл не найден");
                return ResponseEntity.ok(result);
            }

            result.put("found", true);
            result.put("filename", file.getFilename());
            result.put("length", file.getLength());
            result.put("contentType", file.getMetadata() != null ?
                    file.getMetadata().get("_contentType") : "unknown");
            result.put("uploadDate", file.getUploadDate());

            // Пробуем прочитать первые байты для проверки
            GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDatabaseFactory.getMongoDatabase());
            byte[] bytes = IOUtils.toByteArray(gridFSBucket.openDownloadStream(objectId));
            result.put("bytesRead", bytes.length);
            result.put("isReadable", true);

        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("errorType", e.getClass().getName());
        }
        return ResponseEntity.ok(result);
    }
}