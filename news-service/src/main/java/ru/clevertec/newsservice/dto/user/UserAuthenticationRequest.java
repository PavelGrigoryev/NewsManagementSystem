package ru.clevertec.newsservice.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserAuthenticationRequest(@Email
                                        @NotBlank
                                        @Size(max = 64)
                                        String email,

                                        @NotBlank
                                        @Size(max = 64)
                                        String password) {
}
