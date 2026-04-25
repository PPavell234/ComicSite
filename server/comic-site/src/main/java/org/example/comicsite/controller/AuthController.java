package org.example.comicsite.controller;

import org.example.comicsite.dto.AuthResponse;
import org.example.comicsite.dto.LoginRequest;
import org.example.comicsite.dto.RegisterRequest;
import org.example.comicsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserService userService;

    // Регистрация
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        if (response.getMessage().contains("успешна")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    // Вход
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        if (response.getMessage().contains("успешно")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    // Выход
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        boolean success = userService.logout(token);
        if (success) {
            return ResponseEntity.ok("Выход выполнен успешно");
        }
        return ResponseEntity.badRequest().body("Ошибка при выходе");
    }
}