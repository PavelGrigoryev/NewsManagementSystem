package ru.clevertec.newsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.newsservice.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
