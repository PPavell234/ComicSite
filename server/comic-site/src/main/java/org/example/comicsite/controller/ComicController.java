package org.example.comicsite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.comicsite.model.Comic;
import org.example.comicsite.repository.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comics")
public class ComicController {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 1. СОЗДАНИЕ КОМИКСА (с файлами)
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createComic(
            @RequestParam("userId") String userId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tags") String tags,  // Принимаем как строку с запятыми
            @RequestParam("year") Integer year,
            @RequestParam("translator") String translator,
            @RequestParam("artist") String artist,
            @RequestParam(value = "cover", required = false) MultipartFile coverFile,
            @RequestParam("pdf") MultipartFile pdfFile) {

        try {
            // Логируем полученные данные
            System.out.println("Создание комикса: " + title);
            System.out.println("PDF файл: " + pdfFile.getOriginalFilename());
            if (coverFile != null) {
                System.out.println("Обложка: " + coverFile.getOriginalFilename());
            } else {
                System.out.println("Обложка не загружена, будет использована первая страница PDF");
            }

            // 1. Сохраняем PDF в GridFS
            String pdfId = gridFsTemplate.store(
                    pdfFile.getInputStream(),
                    pdfFile.getOriginalFilename(),
                    pdfFile.getContentType()
            ).toString();

            // 2. Сохраняем обложку (если есть)
            String coverId = null;
            if (coverFile != null && !coverFile.isEmpty()) {
                coverId = gridFsTemplate.store(
                        coverFile.getInputStream(),
                        coverFile.getOriginalFilename(),
                        coverFile.getContentType()
                ).toString();
            }

            // 3. Создаем комикс
            Comic comic = new Comic();
            comic.setUserId(userId);
            comic.setTitle(title);
            comic.setDescription(description);

            // Разбиваем теги
            List<String> tagList = Arrays.asList(tags.split(","));
            comic.setTags(tagList);

            comic.setYear(year);
            comic.setTranslator(translator);
            comic.setArtist(artist);
            comic.setCoverImageId(coverId);
            comic.setPdfFileId(pdfId);

            // 4. Сохраняем в БД
            Comic savedComic = comicRepository.save(comic);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Комикс успешно создан!");
            response.put("id", savedComic.getId());
            response.put("title", savedComic.getTitle());
            response.put("pdfFileId", pdfId);
            response.put("coverImageId", coverId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка при создании комикса: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 2. ПОЛУЧИТЬ ВСЕ КОМИКСЫ
    @GetMapping("/all")
    public List<Comic> getAllComics() {
        return comicRepository.findAll();
    }

    // 3. ПОЛУЧИТЬ КОМИКС ПО ID
    @GetMapping("/{id}")
    public Comic getComicById(@PathVariable String id) {
        return comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Комикс не найден"));
    }
}