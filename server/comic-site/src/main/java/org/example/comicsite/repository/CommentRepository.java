package org.example.comicsite.repository;

import org.example.comicsite.model.CommentDB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<CommentDB, String> {

    // Найти все комментарии по ID комикса (сортировка по дате)
    List<CommentDB> findByComicIdOrderByCreatedAtDesc(String comicId);

    // Найти все комментарии по ID комикса
    List<CommentDB> findByComicId(String comicId);

    // Найти все комментарии по ID пользователя
    List<CommentDB> findByUserId(String userId);

    // Подсчитать количество комментариев у комикса
    long countByComicId(String comicId);
}