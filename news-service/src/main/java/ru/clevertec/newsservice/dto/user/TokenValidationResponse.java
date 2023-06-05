package ru.clevertec.newsservice.dto.user;

public record TokenValidationResponse(String role,
                                      String email) {
}
