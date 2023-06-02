package ru.clevertec.userservice.dto;

public record AuthenticationRequest(String email,
                                    String password) {
}
