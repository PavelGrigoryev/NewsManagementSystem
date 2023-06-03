package ru.clevertec.newsservice.dto.user;

public record RegisterRequest(String firstname,
                              String lastname,
                              String email,
                              String password,
                              Role role) {
}
