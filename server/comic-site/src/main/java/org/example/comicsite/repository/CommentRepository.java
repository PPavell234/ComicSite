package org.example.comicsite.repository;

import org.example.comicsite.model.CommentDB;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<CommentDB, String> {

    // Поиск по комиксу и странице
    List<CommentDB> findByComicIdAndPageNumberOrderByCreatedAtDesc(String comicId, int pageNumber);

    // Поиск только по комиксу (для совместимости)
    List<CommentDB> findByComicIdOrderByCreatedAtDesc(String comicId);

    List<CommentDB> findByUserId(String userId);

    long countByComicId(String comicId);
}