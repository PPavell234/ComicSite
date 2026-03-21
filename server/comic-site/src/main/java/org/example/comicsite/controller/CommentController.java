package org.example.comicsite.controller;

import org.example.comicsite.model.CommentDB;
import org.example.comicsite.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    // 1. ПОЛУЧИТЬ КОММЕНТАРИИ ПО ID КОМИКСА (с пагинацией 25)
    @GetMapping("/comic/{comicId}")
    public ResponseEntity<?> getCommentsByComic(
            @PathVariable String comicId,
            @RequestParam(defaultValue = "0") int page) {

        try {
            // Получаем все комментарии комикса, отсортированные по дате (новые сверху)
            List<CommentDB> allComments = commentRepository.findByComicIdOrderByCreatedAtDesc(comicId);

            // Пагинация: 25 комментариев на странице
            int start = page * 25;
            int end = Math.min(start + 25, allComments.size());

            List<CommentDB> paginatedComments;
            if (start < allComments.size()) {
                paginatedComments = allComments.subList(start, end);
            } else {
                paginatedComments = List.of();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("comments", paginatedComments);
            response.put("total", allComments.size());
            response.put("hasMore", end < allComments.size());
            response.put("currentPage", page);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка загрузки комментариев: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 2. СОЗДАТЬ КОММЕНТАРИЙ
    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentDB comment) {
        try {
            comment.setCreatedAt(LocalDateTime.now());
            comment.setUpdatedAt(LocalDateTime.now());
            comment.setLikes(0);
            comment.setDislikes(0);
            comment.setEdited(false);

            if (comment.getReplies() == null) {
                comment.setReplies(new java.util.ArrayList<>());
            }

            CommentDB saved = commentRepository.save(comment);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка создания комментария: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 3. ДОБАВИТЬ ОТВЕТ НА КОММЕНТАРИЙ
    @PostMapping("/{commentId}/reply")
    public ResponseEntity<?> addReply(
            @PathVariable String commentId,
            @RequestBody CommentReply reply) {

        try {
            CommentDB comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

            reply.setCreatedAt(LocalDateTime.now());
            comment.getReplies().add(reply);
            comment.setUpdatedAt(LocalDateTime.now());

            commentRepository.save(comment);

            return ResponseEntity.ok(reply);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка добавления ответа: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 4. ДОБАВИТЬ ЛАЙК/ДИЗЛАЙК
    @PostMapping("/{commentId}/reaction")
    public ResponseEntity<?> addReaction(
            @PathVariable String commentId,
            @RequestParam boolean like) {

        try {
            CommentDB comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

            if (like) {
                comment.setLikes(comment.getLikes() + 1);
            } else {
                comment.setDislikes(comment.getDislikes() + 1);
            }

            commentRepository.save(comment);

            Map<String, Object> response = new HashMap<>();
            response.put("likes", comment.getLikes());
            response.put("dislikes", comment.getDislikes());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 5. УДАЛИТЬ КОММЕНТАРИЙ (только для админа/модератора)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId) {
        try {
            CommentDB comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

            commentRepository.delete(comment);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Комментарий удален");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка удаления: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 6. ПОЛУЧИТЬ КОММЕНТАРИЙ ПО ID
    @GetMapping("/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable String commentId) {
        try {
            CommentDB comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Комментарий не найден"));
            return ResponseEntity.ok(comment);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 7. ПОЛУЧИТЬ ВСЕ КОММЕНТАРИИ ПОЛЬЗОВАТЕЛЯ
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCommentsByUser(@PathVariable String userId) {
        try {
            List<CommentDB> comments = commentRepository.findByUserId(userId);
            return ResponseEntity.ok(comments);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // 8. ПОЛУЧИТЬ КОЛИЧЕСТВО КОММЕНТАРИЕВ У КОМИКСА
    @GetMapping("/comic/{comicId}/count")
    public ResponseEntity<?> getCommentCount(@PathVariable String comicId) {
        try {
            long count = commentRepository.countByComicId(comicId);
            Map<String, Object> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}