package ru.clevertec.newsservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.newsservice.dto.comment.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse findById(Long id);

    List<CommentResponse> findAllByNewsId(Long newsId, Pageable pageable);

}
