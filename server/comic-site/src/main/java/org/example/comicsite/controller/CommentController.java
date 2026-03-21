package org.example.comicsite.controller;

import org.example.comicsite.model.CommentDB;
import org.example.comicsite.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/comic/{comicId}")
    public ResponseEntity<?> getCommentsByComic(
            @PathVariable String comicId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String userId) {

        try {
            List<CommentDB> allComments = commentRepository.findByComicIdOrderByCreatedAtDesc(comicId);
            int start = page * 25;
            int end = Math.min(start + 25, allComments.size());

            List<CommentDB> paginatedComments;
            if (start < allComments.size()) {
                paginatedComments = allComments.subList(start, end);

                // Для каждого комментария добавляем информацию о реакции текущего пользователя
                if (!userId.isEmpty()) {
                    for (CommentDB comment : paginatedComments) {
                        String reaction = "none";
                        if (comment.getLikedBy() != null && comment.getLikedBy().contains(userId)) {
                            reaction = "like";
                        } else if (comment.getDislikedBy() != null && comment.getDislikedBy().contains(userId)) {
                            reaction = "dislike";
                        }
                        comment.setUserReaction(reaction);
                    }
                }
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

    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentDB comment) {
        try {
            comment.setCreatedAt(LocalDateTime.now());
            comment.setUpdatedAt(LocalDateTime.now());
            comment.setLikes(0);
            comment.setDislikes(0);
            comment.setEdited(false);
            comment.setLikedBy(new ArrayList<>());
            comment.setDislikedBy(new ArrayList<>());

            if (comment.getReplies() == null) {
                comment.setReplies(new ArrayList<>());
            }

            CommentDB saved = commentRepository.save(comment);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка создания комментария: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/{commentId}/reply")
    public ResponseEntity<?> addReply(
            @PathVariable String commentId,
            @RequestBody CommentReply reply) {

        try {
            CommentDB comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

            reply.setId(UUID.randomUUID().toString());
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

    @PostMapping("/{commentId}/reaction")
    public ResponseEntity<?> addReaction(
            @PathVariable String commentId,
            @RequestParam boolean like,
            @RequestParam String userId) {

        try {
            CommentDB comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Комментарий не найден"));

            // Проверяем, ставил ли пользователь уже реакцию
            boolean alreadyLiked = comment.getLikedBy() != null && comment.getLikedBy().contains(userId);
            boolean alreadyDisliked = comment.getDislikedBy() != null && comment.getDislikedBy().contains(userId);

            if (like) {
                // Если уже лайкнул - убираем лайк
                if (alreadyLiked) {
                    comment.setLikes(comment.getLikes() - 1);
                    comment.getLikedBy().remove(userId);
                }
                // Если был дизлайк - убираем дизлайк и ставим лайк
                else if (alreadyDisliked) {
                    comment.setDislikes(comment.getDislikes() - 1);
                    comment.getDislikedBy().remove(userId);
                    comment.setLikes(comment.getLikes() + 1);
                    comment.getLikedBy().add(userId);
                }
                // Нет реакции - ставим лайк
                else {
                    comment.setLikes(comment.getLikes() + 1);
                    comment.getLikedBy().add(userId);
                }
            } else {
                // Если уже дизлайкнул - убираем дизлайк
                if (alreadyDisliked) {
                    comment.setDislikes(comment.getDislikes() - 1);
                    comment.getDislikedBy().remove(userId);
                }
                // Если был лайк - убираем лайк и ставим дизлайк
                else if (alreadyLiked) {
                    comment.setLikes(comment.getLikes() - 1);
                    comment.getLikedBy().remove(userId);
                    comment.setDislikes(comment.getDislikes() + 1);
                    comment.getDislikedBy().add(userId);
                }
                // Нет реакции - ставим дизлайк
                else {
                    comment.setDislikes(comment.getDislikes() + 1);
                    comment.getDislikedBy().add(userId);
                }
            }

            commentRepository.save(comment);

            Map<String, Object> response = new HashMap<>();
            response.put("likes", comment.getLikes());
            response.put("dislikes", comment.getDislikes());
            response.put("userLiked", comment.getLikedBy() != null && comment.getLikedBy().contains(userId));
            response.put("userDisliked", comment.getDislikedBy() != null && comment.getDislikedBy().contains(userId));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ошибка: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    private String getUserReaction(CommentDB comment, String userId) {
        if (comment.getLikedBy() != null && comment.getLikedBy().contains(userId)) {
            return "like";
        }
        if (comment.getDislikedBy() != null && comment.getDislikedBy().contains(userId)) {
            return "dislike";
        }
        return "none";
    }

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