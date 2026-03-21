package org.example.comicsite.controller;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDB {

    @Id
    private String id;

    // Связь с пользователем
    private String userId;           // ID пользователя (FK)
    private String userName;         // Имя пользователя (для быстрого отображения)
    private String userAvatar;       // Аватар пользователя (опционально)

    // Связь с комиксом
    private String comicId;          // ID комикса (FK)
    private String comicTitle;       // Название комикса (для быстрого отображения)
    private String comicPageUrl;     // Ссылка на страницу комикса

    // Содержание комментария
    private String content;          // Текст комментария

    // Статистика
    private int likes;               // Количество лайков
    private int dislikes;            // Количество дизлайков

    // Вложенные комментарии (ответы)
    private List<CommentReply> replies;  // Ответы на комментарий

    // Метаданные
    private LocalDateTime createdAt;     // Дата создания
    private LocalDateTime updatedAt;     // Дата последнего обновления
    private boolean isEdited;            // Был ли отредактирован

    // Конструктор
    public CommentDB() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.likes = 0;
        this.dislikes = 0;
        this.isEdited = false;
        this.replies = new ArrayList<>();
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserAvatar() { return userAvatar; }
    public void setUserAvatar(String userAvatar) { this.userAvatar = userAvatar; }

    public String getComicId() { return comicId; }
    public void setComicId(String comicId) { this.comicId = comicId; }

    public String getComicTitle() { return comicTitle; }
    public void setComicTitle(String comicTitle) { this.comicTitle = comicTitle; }

    public String getComicPageUrl() { return comicPageUrl; }
    public void setComicPageUrl(String comicPageUrl) { this.comicPageUrl = comicPageUrl; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public int getDislikes() { return dislikes; }
    public void setDislikes(int dislikes) { this.dislikes = dislikes; }

    public List<CommentReply> getReplies() { return replies; }
    public void setReplies(List<CommentReply> replies) { this.replies = replies; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isEdited() { return isEdited; }
    public void setEdited(boolean edited) { isEdited = edited; }
}
