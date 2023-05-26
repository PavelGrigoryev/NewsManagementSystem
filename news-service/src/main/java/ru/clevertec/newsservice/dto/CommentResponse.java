package ru.clevertec.newsservice.dto;

import java.time.LocalDateTime;

public record CommentResponse(Long id,
                              LocalDateTime time,
                              String text,
                              String username) {
}
