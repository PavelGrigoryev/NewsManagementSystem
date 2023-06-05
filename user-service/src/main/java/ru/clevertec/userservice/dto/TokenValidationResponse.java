package ru.clevertec.userservice.dto;

public record TokenValidationResponse(String role,
                                      String email) {
}
