package ru.clevertec.newsservice.dto.news;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public record NewsResponse(Long id,

                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
                           LocalDateTime time,

                           String title,
                           String text,
                           String email) implements Serializable {
}
