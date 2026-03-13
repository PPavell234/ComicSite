package org.example.comicsite.controller;

import org.example.comicsite.model.Comic;
import org.example.comicsite.repository.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/comics")
public class ComicController {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @PostMapping("/create")
    public String createComic(@RequestBody Comic comic) {  // Только JSON, без файлов
        // Пока просто сохраняем JSON данные
        Comic savedComic = comicRepository.save(comic);
        return "{\"message\": \"Комикс сохранен без файлов\", \"id\": \"" + savedComic.getId() + "\"}";
    }

    // Отдельный метод для загрузки файлов
    @PostMapping("/upload-files/{comicId}")
    public String uploadFiles(
            @PathVariable String comicId,
            @RequestParam("cover") MultipartFile cover,
            @RequestParam("pdf") MultipartFile pdf) throws IOException {

        // Находим комикс
        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new RuntimeException("Комикс не найден"));

        // Сохраняем файлы и обновляем комикс
        String coverId = gridFsTemplate.store(cover.getInputStream(), cover.getOriginalFilename(), cover.getContentType()).toString();
        String pdfId = gridFsTemplate.store(pdf.getInputStream(), pdf.getOriginalFilename(), pdf.getContentType()).toString();

        comic.setCoverImageId(coverId);
        comic.setPdfFileId(pdfId);
        comicRepository.save(comic);

        return "{\"message\": \"Файлы загружены\"}";
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/debug-db")
    public String debugDatabase() {
        String dbName = mongoTemplate.getDb().getName();
        String collectionName = mongoTemplate.getCollectionName(Comic.class);

        return "Используется БД: " + dbName + ", коллекция: " + collectionName;
    }
}