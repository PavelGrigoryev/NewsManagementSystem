package ru.clevertec.userservice.dto;

import ru.clevertec.userservice.model.Role;

public record RegisterRequest(String firstname,
                              String lastname,
                              String email,
                              String password,
                              Role role) {
}
