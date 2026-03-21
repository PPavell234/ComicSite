package org.example.comicsite.model;

import org.example.comicsite.controller.CommentReply;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "comments")
public class CommentDB {

    @Id
    private String id;
    private String userId;
    private String userName;
    private String userAvatar;
    private String comicId;
    private String comicTitle;
    private String comicPageUrl;
    private String content;
    private int likes;
    private int dislikes;

    // Храним ID пользователей, которые поставили лайк/дизлайк
    private List<String> likedBy;
    private List<String> dislikedBy;

    // Временное поле для фронтенда (не сохраняется в БД)
    private transient String userReaction;  // "like", "dislike", или "none"

    private List<CommentReply> replies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isEdited;

    public CommentDB() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.likes = 0;
        this.dislikes = 0;
        this.isEdited = false;
        this.replies = new ArrayList<>();
        this.likedBy = new ArrayList<>();
        this.dislikedBy = new ArrayList<>();
        this.userReaction = "none";
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

    public List<String> getLikedBy() { return likedBy; }
    public void setLikedBy(List<String> likedBy) { this.likedBy = likedBy; }

    public List<String> getDislikedBy() { return dislikedBy; }
    public void setDislikedBy(List<String> dislikedBy) { this.dislikedBy = dislikedBy; }

    public String getUserReaction() { return userReaction; }
    public void setUserReaction(String userReaction) { this.userReaction = userReaction; }

    public List<CommentReply> getReplies() { return replies; }
    public void setReplies(List<CommentReply> replies) { this.replies = replies; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isEdited() { return isEdited; }
    public void setEdited(boolean edited) { isEdited = edited; }
}