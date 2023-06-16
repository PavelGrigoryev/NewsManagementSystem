package ru.clevertec.userservice.service;

import ru.clevertec.userservice.dto.proto.DeleteResponse;
import ru.clevertec.userservice.dto.proto.TokenValidationResponse;
import ru.clevertec.userservice.dto.proto.UserResponse;
import ru.clevertec.userservice.dto.proto.UserAuthenticationRequest;
import ru.clevertec.userservice.dto.proto.UserRegisterRequest;
import ru.clevertec.userservice.dto.proto.UserUpdateRequest;

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
