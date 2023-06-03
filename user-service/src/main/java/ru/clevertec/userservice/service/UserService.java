package ru.clevertec.userservice.service;

import ru.clevertec.userservice.dto.AuthenticationRequest;
import ru.clevertec.userservice.dto.RegisterRequest;
import ru.clevertec.userservice.dto.RoleResponse;
import ru.clevertec.userservice.dto.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);

    UserResponse authenticate(AuthenticationRequest request);

    RoleResponse tokenValidationCheck(String token);

}
