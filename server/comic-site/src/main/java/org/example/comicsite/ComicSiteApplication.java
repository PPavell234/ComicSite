package org.example.comicsite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ComicSiteApplication {
    public static void main(String[] args) {
        // Принудительно устанавливаем свойства
        System.setProperty("spring.data.mongodb.database", "comicsite");
        System.setProperty("spring.data.mongodb.host", "localhost");
        System.setProperty("spring.data.mongodb.port", "27017");


        SpringApplication.run(ComicSiteApplication.class, args);
    }
}
