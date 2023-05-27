package ru.clevertec.newsservice.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentWithNewsRequest(String text,
                                     String username,
                                     @JsonProperty("news_id")
                                     Long newsId) {
}
