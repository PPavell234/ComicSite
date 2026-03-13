package org.example.comicsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/db-info")
    public Map<String, Object> getDbInfo() {
        Map<String, Object> info = new HashMap<>();

        try {
            // 1. Какая БД используется сейчас?
            String currentDb = mongoTemplate.getDb().getName();
            info.put("текущая_бд", currentDb);

            // 2. Название коллекции для класса Comic
            try {
                String collectionName = mongoTemplate.getCollectionName(org.example.comicsite.model.Comic.class);
                info.put("коллекция_comic", collectionName);
            } catch (Exception e) {
                info.put("коллекция_comic", "ошибка: " + e.getMessage());
            }

            // 3. Все доступные базы данных (упрощенный вариант)
            List<String> databases = new ArrayList<>();
            try {
                // Пробуем получить через комманду
                var result = mongoTemplate.executeCommand("{ listDatabases: 1 }");
                if (result.containsKey("databases")) {
                    var dbs = (List<Map<String, Object>>) result.get("databases");
                    for (Map<String, Object> db : dbs) {
                        databases.add((String) db.get("name"));
                    }
                }
            } catch (Exception e) {
                databases.add("не удалось получить список: " + e.getMessage());
            }
            info.put("все_бд", databases);

            // 4. System properties связанные с MongoDB
            Map<String, String> props = new HashMap<>();
            props.put("spring.data.mongodb.database",
                    System.getProperty("spring.data.mongodb.database", "не задано"));
            props.put("spring.data.mongodb.uri",
                    System.getProperty("spring.data.mongodb.uri", "не задано"));
            props.put("spring.data.mongodb.host",
                    System.getProperty("spring.data.mongodb.host", "не задано"));
            info.put("system_properties", props);

        } catch (Exception e) {
            info.put("ошибка", e.getMessage());
            e.printStackTrace();
        }

        return info;
    }

    @PostMapping("/test-save")
    public Map<String, Object> testSave() {
        Map<String, Object> result = new HashMap<>();

        try {
            // Создаем тестовый документ
            Map<String, Object> testDoc = new HashMap<>();
            testDoc.put("test", "data");
            testDoc.put("timestamp", System.currentTimeMillis());
            testDoc.put("source", "debug-controller");

            // Сохраняем в текущую БД
            var saved = mongoTemplate.save(testDoc, "debug_test");

            result.put("статус", "успешно");
            result.put("сохранено_в_бд", mongoTemplate.getDb().getName());
            result.put("коллекция", "debug_test");
            result.put("id", saved.get("_id").toString());

        } catch (Exception e) {
            result.put("статус", "ошибка");
            result.put("ошибка", e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping("/simple-check")
    public String simpleCheck() {
        return "Текущая БД: " + mongoTemplate.getDb().getName();
    }
}