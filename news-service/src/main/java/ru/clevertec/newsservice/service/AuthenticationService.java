package ru.clevertec.newsservice.service;

import ru.clevertec.newsservice.dto.user.Role;
import ru.clevertec.newsservice.dto.user.TokenValidationResponse;

public interface AuthenticationService {

    TokenValidationResponse checkTokenValidationForRole(String token, Role role);

    Boolean isObjectOwnedByEmailAndRole(String userRole, Role expectedRole, String userEmail, String objectEmail);

}
