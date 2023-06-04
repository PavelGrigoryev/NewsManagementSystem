package ru.clevertec.newsservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@Size(min = 2, max = 30)
                              @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ]+$",
                                      message = "Firstname must contain only letters of the Russian and English " +
                                                "alphabets without spaces in any case")
                              String firstname,

                              @Size(min = 2, max = 30)
                              @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ]+$",
                                      message = "Lastname must contain only letters of the Russian and English " +
                                                "alphabets without spaces in any case")
                              String lastname,

                              @NotBlank
                              @Email
                              String email,

                              @NotBlank
                              String password,

                              @Pattern(regexp = "ADMIN|JOURNALIST|SUBSCRIBER",
                                      message = "Acceptable roles are only: ADMIN, JOURNALIST, SUBSCRIBER")
                              String role) {
}
