package ru.clevertec.newsservice.model.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import ru.clevertec.newsservice.model.Comment;

import java.time.LocalDateTime;

public class CommentListener {

    @PrePersist
    public void prePersist(Comment comment) {
        comment.setTime(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate(Comment comment) {
        comment.setTime(LocalDateTime.now());
    }

}
