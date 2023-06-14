package ru.clevertec.newsservice.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public record CommentResponse(Long id,

                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
                              LocalDateTime time,

                              String text,
                              String username,
                              String email) implements Serializable {
}
