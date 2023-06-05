package ru.clevertec.newsservice.dto.comment;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CommentRequest(@Size(min = 3, max = 500)
                             String text,

                             @Size(min = 2, max = 30)
                             @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ0-9@_-]+$",
                                     message = "This field must contain only letters of the Russian " +
                                               "and English alphabets, numbers, symbols(@ _ -) without spaces in any case")
                             String username) {
}
