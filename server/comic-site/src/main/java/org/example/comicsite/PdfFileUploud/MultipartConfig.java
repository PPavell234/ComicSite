package org.example.comicsite.PdfFileUploud;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import jakarta.servlet.MultipartConfigElement;

@Configuration  // Помечает класс как источник конфигурации Spring
public class MultipartConfig {

    @Bean  // Создает и регистрирует бин в Spring контексте
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        // Устанавливает максимальный размер одного файла - 50 МБ
        factory.setMaxFileSize(DataSize.ofMegabytes(50));

        // Устанавливает максимальный размер всего запроса (включая все файлы и данные формы) - 50 МБ
        factory.setMaxRequestSize(DataSize.ofMegabytes(50));

        return factory.createMultipartConfig();
    }
}