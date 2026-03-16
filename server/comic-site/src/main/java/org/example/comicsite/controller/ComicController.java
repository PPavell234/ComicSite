package org.example.comicsite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.comicsite.model.Comic;
import org.example.comicsite.repository.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/comics")
public class ComicController {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 1. СОЗДАНИЕ КОМИКСА С ФАЙЛАМИ (ОДНИМ ЗАПРОСОМ)
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public Comic createComicWithFiles(
            @RequestParam("comic") String comicJson,
            @RequestParam("cover") MultipartFile cover,
            @RequestParam("pdf") MultipartFile pdf) throws IOException {

        // 1. Парсим JSON в объект Comic
        Comic comic = objectMapper.readValue(comicJson, Comic.class);

        // 2. Сохраняем обложку в GridFS
        String coverId = gridFsTemplate.store(
                cover.getInputStream(),
                cover.getOriginalFilename(),
                cover.getContentType()
        ).toString();

        // 3. Сохраняем PDF в GridFS
        String pdfId = gridFsTemplate.store(
                pdf.getInputStream(),
                pdf.getOriginalFilename(),
                pdf.getContentType()
        ).toString();

        // 4. Устанавливаем ID файлов в комикс
        comic.setCoverImageId(coverId);
        comic.setPdfFileId(pdfId);

        // 5. Сохраняем комикс в БД
        return comicRepository.save(comic);
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

    // 4. ПОЛУЧИТЬ ФАЙЛ ПО ID
    @GetMapping("/file/{fileId}")
    public String getFileInfo(@PathVariable String fileId) {
        return "Файл с ID: " + fileId + " (нужно реализовать скачивание)";
    }

    // 5. ТЕСТОВЫЙ МЕТОД
    @GetMapping("/test")
    public String test() {
        return "{\"status\": \"OK\", \"message\": \"Сервер работает\"}";
    }
}