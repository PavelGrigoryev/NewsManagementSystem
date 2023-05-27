package ru.clevertec.newsservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.comment.CommentRequest;
import ru.clevertec.newsservice.dto.comment.CommentResponse;
import ru.clevertec.newsservice.dto.comment.CommentWithNewsRequest;
import ru.clevertec.newsservice.dto.news.NewsWithCommentsResponse;

import java.util.List;

/**
 * The CommentService interface provides the implementation for CRUD operations.
 */
public interface CommentService {

    CommentResponse findById(Long id);

    NewsWithCommentsResponse findNewsByNewsIdWithComments(Long newsId, Pageable pageable);

    List<CommentResponse> findAllByMatchingTextParams(CommentRequest commentRequest, Pageable pageable);

    CommentResponse save(CommentWithNewsRequest commentWithNewsRequest);

    CommentResponse updateById(Long id, CommentRequest commentRequest);

    DeleteResponse deleteById(Long id);

}
