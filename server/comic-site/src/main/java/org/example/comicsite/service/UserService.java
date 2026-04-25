package org.example.comicsite.service;

import org.example.comicsite.dto.AuthResponse;
import org.example.comicsite.dto.LoginRequest;
import org.example.comicsite.dto.RegisterRequest;
import org.example.comicsite.model.User;
import org.example.comicsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Хранилище токенов (в реальном проекте используйте JWT или Redis)
    private final Map<String, String> tokenStore = new HashMap<>();

    // Регистрация
    public AuthResponse register(RegisterRequest request) {
        // Проверяем, существует ли пользователь с таким email
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(null, null, null, "Пользователь с таким email уже существует");
        }

        // Создаем нового пользователя
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        // Генерируем токен
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, user.getEmail());

        return new AuthResponse(token, user.getEmail(), user.getNickname(), "Регистрация успешна!");
    }

    // Вход
    public AuthResponse login(LoginRequest request) {
        // Ищем пользователя по email
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return new AuthResponse(null, null, null, "Пользователь не найден");
        }

        // Проверяем пароль
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(null, null, null, "Неверный пароль");
        }

        // Генерируем новый токен
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, user.getEmail());

        return new AuthResponse(token, user.getEmail(), user.getNickname(), "Вход выполнен успешно!");
    }

    // Выход
    public boolean logout(String token) {
        return tokenStore.remove(token) != null;
    }

    // Проверка токена (валидация)
    public String validateToken(String token) {
        return tokenStore.get(token);
    }
}