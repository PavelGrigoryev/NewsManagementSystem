package ru.clevertec.newsservice.dto.news;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewsRequest(@NotBlank
                          @Size(max = 255)
                          String title,

                          @NotBlank
                          String text) {
}
