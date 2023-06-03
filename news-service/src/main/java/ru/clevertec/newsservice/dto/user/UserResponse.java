package ru.clevertec.newsservice.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "firstname",
        "lastname",
        "email",
        "role",
        "token",
        "token_expiration",
        "created_time",
        "updated_time"
})
public record UserResponse(Long id,
                           String firstname,
                           String lastname,
                           String email,
                           Role role,
                           String token,

                           @JsonProperty("token_expiration")
                           String tokenExpiration,

                           @JsonProperty("created_time")
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
                           LocalDateTime createdTime,

                           @JsonProperty("updated_time")
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
                           LocalDateTime updatedTime
) {
}
