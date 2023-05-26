package ru.clevertec.newsservice.dto.news;

import ru.clevertec.newsservice.dto.comment.CommentResponse;

import java.time.LocalDateTime;
import java.util.List;

public record NewsWithCommentsResponse(Long id,
                                       LocalDateTime time,
                                       String title,
                                       String text,
                                       List<CommentResponse> comments) {
}
