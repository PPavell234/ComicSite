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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comics")
@CrossOrigin(origins = "http://localhost:5173")
public class ComicController {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoDatabaseFactory mongoDatabaseFactory;

    // СОЗДАНИЕ КОМИКСА С СОХРАНЕНИЕМ СТРАНИЦ
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

            // 1. Сохраняем обложку в GridFS
            String coverId = gridFsTemplate.store(
                    coverFile.getInputStream(),
                    coverFile.getOriginalFilename(),
                    coverFile.getContentType()
            ).toString();
            System.out.println("Cover saved with ID: " + coverId);

            // 2. Сохраняем PDF в GridFS
            String pdfId = gridFsTemplate.store(
                    pdfFile.getInputStream(),
                    pdfFile.getOriginalFilename(),
                    pdfFile.getContentType()
            ).toString();
            System.out.println("PDF saved with ID: " + pdfId);

            // 3. Конвертируем PDF в страницы и сохраняем в GridFS
            List<String> pageImageIds = new ArrayList<>();
            try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                int totalPages = document.getNumberOfPages();
                System.out.println("Всего страниц для конвертации: " + totalPages);

                for (int page = 0; page < totalPages; page++) {
                    System.out.println("Конвертация страницы " + (page + 1));
                    BufferedImage image = pdfRenderer.renderImageWithDPI(page, 150);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(image, "jpg", baos);

                    ObjectId pageId = gridFsTemplate.store(
                            new ByteArrayInputStream(baos.toByteArray()),
                            "page_" + (page + 1) + ".jpg",
                            "image/jpeg"
                    );
                    pageImageIds.add(pageId.toString());
                }
                System.out.println("Все страницы сконвертированы: " + pageImageIds.size());
            }

            // 4. Создаем комикс
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
            comic.setCoverImageId(coverId);
            comic.setPdfFileId(pdfId);
            comic.setPageImageIds(pageImageIds);

            // 5. Сохраняем в БД
            Comic savedComic = comicRepository.save(comic);
            System.out.println("Comic saved with ID: " + savedComic.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Комикс успешно создан!");
            response.put("id", savedComic.getId());
            response.put("title", savedComic.getTitle());
            response.put("coverImageId", coverId);
            response.put("pdfFileId", pdfId);
            response.put("pageImageIds", pageImageIds);
            response.put("totalPages", pageImageIds.size());

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

    // ПОЛУЧИТЬ КОМИКС ПО ID
    @GetMapping("/{id}")
    public ResponseEntity<Comic> getComicById(@PathVariable String id) {
        System.out.println("Запрос комикса с ID: " + id);

        return comicRepository.findById(id)
                .map(comic -> {
                    System.out.println("Комикс найден: " + comic.getTitle());
                    System.out.println("Страниц в комиксе: " + (comic.getPageImageIds() != null ? comic.getPageImageIds().size() : 0));
                    return ResponseEntity.ok(comic);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ПОЛУЧИТЬ ФАЙЛ ПО ID (обложка, страницы, PDF)
    @GetMapping("/files/{fileId}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileId) {
        try {
            System.out.println("Запрос файла с ID: " + fileId);

            ObjectId objectId;
            try {
                objectId = new ObjectId(fileId);
            } catch (IllegalArgumentException e) {
                System.out.println("Невалидный ObjectId: " + fileId);
                return ResponseEntity.badRequest().build();
            }

            var file = gridFsTemplate.findOne(
                    Query.query(Criteria.where("_id").is(objectId))
            );

            if (file == null) {
                System.out.println("Файл не найден в GridFS: " + fileId);
                return ResponseEntity.notFound().build();
            }

            System.out.println("Файл найден: " + file.getFilename());

            GridFSBucket gridFSBucket = GridFSBuckets.create(mongoDatabaseFactory.getMongoDatabase());
            byte[] bytes = IOUtils.toByteArray(gridFSBucket.openDownloadStream(objectId));

            HttpHeaders headers = new HttpHeaders();
            String contentType = "image/jpeg";
            if (file.getMetadata() != null && file.getMetadata().get("_contentType") != null) {
                contentType = file.getMetadata().getString("_contentType");
            }
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(bytes.length);

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

        } catch (Exception e) {
            result.put("error", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}