package ru.clevertec.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.exceptionhandlerstarter.exception.AccessDeniedForThisRoleException;
import ru.clevertec.exceptionhandlerstarter.exception.CantChangeException;
import ru.clevertec.newsservice.client.UserApiClient;
import ru.clevertec.newsservice.dto.user.Role;
import ru.clevertec.newsservice.dto.user.TokenValidationResponse;
import ru.clevertec.newsservice.service.AuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserApiClient userApiClient;

    @Override
    public TokenValidationResponse checkTokenValidationForRole(String token, Role role) {
        TokenValidationResponse response = userApiClient.tokenValidationCheck(token);
        Role userRole = Role.valueOf(response.role());
        if (!userRole.equals(Role.ADMIN) && !userRole.equals(role)) {
            throw new AccessDeniedForThisRoleException("Access Denied for role: " + userRole);
        }
        return response;
    }

    @Override
    public Boolean isObjectOwnedByEmailAndRole(String userRole, Role expectedRole, String userEmail, String objectEmail) {
        Role role = Role.valueOf(userRole);
        if (role.equals(expectedRole) && !userEmail.equals(objectEmail)) {
            throw new CantChangeException("With role " + userRole + " you can add and change/delete only your own news/comments");
        }
        return Boolean.TRUE;
    }

}
