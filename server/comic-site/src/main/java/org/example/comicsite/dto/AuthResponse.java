package org.example.comicsite.dto;

public class AuthResponse {
    private String token;
    private String email;
    private String nickname;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String token, String email, String nickname, String message) {
        this.token = token;
        this.email = email;
        this.nickname = nickname;
        this.message = message;
    }

    // Геттеры и сеттеры
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}