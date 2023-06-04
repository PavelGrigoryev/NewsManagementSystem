package ru.clevertec.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.exceptionhandlerstarter.exception.AccessDeniedForThisRoleException;
import ru.clevertec.newsservice.client.UserApiClient;
import ru.clevertec.newsservice.dto.user.Role;
import ru.clevertec.newsservice.dto.user.RoleResponse;
import ru.clevertec.newsservice.service.AuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserApiClient userApiClient;

    @Override
    public void isRoleAdminOrJournalist(String token) {
        RoleResponse roleResponse = userApiClient.tokenValidationCheck(token);
        Role role = Role.valueOf(roleResponse.role());
        if (!role.equals(Role.ADMIN) && !role.equals(Role.JOURNALIST)) {
            throw new AccessDeniedForThisRoleException("Access Denied for role: " + role);
        }
    }

}
