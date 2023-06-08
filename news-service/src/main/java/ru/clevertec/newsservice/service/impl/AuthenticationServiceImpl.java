package ru.clevertec.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.exceptionhandlerstarter.exception.AccessDeniedForThisRoleException;
import ru.clevertec.exceptionhandlerstarter.exception.UserDoesNotHavePermissionException;
import ru.clevertec.newsservice.client.UserApiClient;
import ru.clevertec.newsservice.dto.user.Role;
import ru.clevertec.newsservice.dto.user.TokenValidationResponse;
import ru.clevertec.newsservice.service.AuthenticationService;

/**
 * The AuthenticationServiceImpl class implements NewsService and provides the implementation for token validation operations.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserApiClient userApiClient;

    /**
     * Checks if the provided token is valid for the given role.
     *
     * @param token The authentication token to be validated.
     * @param role  The expected role for the user associated with the token.
     * @return A {@link TokenValidationResponse} object containing user role and email if token is valid.
     * @throws AccessDeniedForThisRoleException if the user associated with the token does not have the expected role.
     */
    @Override
    public TokenValidationResponse checkTokenValidationForRole(String token, Role role) {
        TokenValidationResponse response = userApiClient.tokenValidationCheck(token);
        Role userRole = Role.valueOf(response.role());
        if (!userRole.equals(Role.ADMIN) && !userRole.equals(role)) {
            throw new AccessDeniedForThisRoleException("Access Denied for role: " + userRole);
        }
        return response;
    }

    /**
     * Checks if the user with the provided email and role owns news/comments with the provided email.
     *
     * @param userRole     The role of the user who is making the request.
     * @param expectedRole The expected role of the user who owns the object.
     * @param userEmail    The email address of the user who is making the request.
     * @param objectEmail  The email address of the news/comments being checked for ownership.
     * @return true if the user with the provided email and role owns the object with the provided email.
     * @throws UserDoesNotHavePermissionException if the user does not have permission to update or delete the news/comments.
     */
    @Override
    public Boolean isObjectOwnedByEmailAndRole(String userRole, Role expectedRole, String userEmail, String objectEmail) {
        Role role = Role.valueOf(userRole);
        if (role.equals(expectedRole) && !userEmail.equals(objectEmail)) {
            throw new UserDoesNotHavePermissionException("With role " + userRole + " you can update or delete only your own news/comments");
        }
        return Boolean.TRUE;
    }

}
