package ru.clevertec.newsservice.dto.news;

import jakarta.validation.constraints.Size;

public record NewsRequest(@Size(min = 3, max = 255)
                          String title,

                          @Size(min = 3, max = 2000)
                          String text) {
}
