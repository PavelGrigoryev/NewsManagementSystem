package ru.clevertec.userservice.service;

import ru.clevertec.userservice.dto.UserAuthenticationRequest;
import ru.clevertec.userservice.dto.DeleteResponse;
import ru.clevertec.userservice.dto.UserRegisterRequest;
import ru.clevertec.userservice.dto.TokenValidationResponse;
import ru.clevertec.userservice.dto.UserUpdateRequest;
import ru.clevertec.userservice.dto.UserResponse;

/**
 * The UserService interface provides the functionality for registering, authenticating, updating, and deleting users.
 */
public interface UserService {

    UserResponse register(UserRegisterRequest request);

    UserResponse authenticate(UserAuthenticationRequest request);

    TokenValidationResponse tokenValidationCheck(String token);

    UserResponse updateByToken(UserUpdateRequest request, String token);

    DeleteResponse deleteByToken(String token);

}
