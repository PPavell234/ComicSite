package org.example.comicsite.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "comics")
public class Comic {

    @Id
    private String id;
    private String userId;
    private String title;
    private String description;
    private List<String> tags;
    private Integer chapterNumber;  // НОВОЕ ПОЛЕ - номер главы
    private Integer year;
    private String translator;
    private String artist;
    private String coverImageId;
    private String pdfFileId;
    private LocalDateTime createdAt;

    public Comic() {
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Integer getChapterNumber() { return chapterNumber; }  // НОВЫЙ ГЕТТЕР
    public void setChapterNumber(Integer chapterNumber) { this.chapterNumber = chapterNumber; }  // НОВЫЙ СЕТТЕР

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getTranslator() { return translator; }
    public void setTranslator(String translator) { this.translator = translator; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getCoverImageId() { return coverImageId; }
    public void setCoverImageId(String coverImageId) { this.coverImageId = coverImageId; }

    public String getPdfFileId() { return pdfFileId; }
    public void setPdfFileId(String pdfFileId) { this.pdfFileId = pdfFileId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}