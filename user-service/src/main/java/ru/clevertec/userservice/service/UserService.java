package ru.clevertec.userservice.service;

import ru.clevertec.userservice.dto.AuthenticationRequest;
import ru.clevertec.userservice.dto.RegisterRequest;
import ru.clevertec.userservice.dto.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest registerRequest);

    UserResponse authenticate(AuthenticationRequest request);

}