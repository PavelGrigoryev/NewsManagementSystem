package ru.clevertec.newsservice.dto;

import java.time.LocalDateTime;

public record NewsResponse(Long id,
                           LocalDateTime time,
                           String title,
                           String text) {
}
