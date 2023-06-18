package ru.clevertec.newsservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.newsservice.dto.proto.CommentRequest;
import ru.clevertec.newsservice.dto.proto.CommentResponse;
import ru.clevertec.newsservice.dto.proto.CommentResponseList;
import ru.clevertec.newsservice.dto.proto.CommentWithNewsRequest;
import ru.clevertec.newsservice.dto.proto.DeleteResponse;
import ru.clevertec.newsservice.dto.proto.NewsWithCommentsResponse;

/**
 * The CommentService interface provides the implementation for CRUD operations.
 */
public interface CommentService {

    CommentResponse findById(Long id);

    NewsWithCommentsResponse findNewsByNewsIdWithComments(Long newsId, Pageable pageable);

    CommentResponseList findAllByMatchingTextParams(String text, String username, Pageable pageable);

    CommentResponse save(CommentWithNewsRequest commentWithNewsRequest, String token);

    CommentResponse updateById(Long id, CommentRequest commentRequest, String token);

    DeleteResponse deleteById(Long id, String token);

}
