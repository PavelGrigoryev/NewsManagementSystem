package ru.clevertec.newsservice.dto.user;

public record AuthenticationRequest(String email,
                                    String password) {
}
