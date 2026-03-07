package org.example.comicsite.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.comicsite.repository.UserRepository;
import org.example.comicsite.model.User;

@RestController
public class TestController {

    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/test")
    public String testMongo() {
        // Попробуем сохранить нового пользователя
        User user = new User("TestUser");
        userRepository.save(user);

        // Посчитаем, сколько пользователей в коллекции
        long count = userRepository.count();

        return "MongoDB is connected! Total users: " + count;
    }
}