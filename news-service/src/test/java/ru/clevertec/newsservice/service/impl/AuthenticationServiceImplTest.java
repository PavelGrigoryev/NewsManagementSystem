package ru.clevertec.newsservice.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.exceptionhandlerstarter.exception.AccessDeniedForThisRoleException;
import ru.clevertec.exceptionhandlerstarter.exception.UserDoesNotHavePermissionException;
import ru.clevertec.newsservice.client.UserApiClient;
import ru.clevertec.newsservice.dto.proto.Role;
import ru.clevertec.newsservice.dto.proto.TokenValidationResponse;
import ru.clevertec.newsservice.util.testbuilder.user.TokenValidationResponseTestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @Mock
    private UserApiClient userApiClient;

    @Test
    @DisplayName("test checkTokenValidationForRole should return expected value")
    void testCheckTokenValidationForRoleShouldReturnExpectedValue() {
        String token = "jwt";
        TokenValidationResponse expectedValue = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();

        doReturn(expectedValue)
                .when(userApiClient)
                .tokenValidationCheck(token);

        TokenValidationResponse actualValue = authenticationService.checkTokenValidationForRole(token, Role.SUBSCRIBER);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test checkTokenValidationForRole should throw AccessDeniedForThisRoleException")
    void testCheckTokenValidationForRoleShouldThrowAccessDeniedForThisRoleException() {
        String token = "jwt";
        TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                .withRole(Role.SUBSCRIBER.name())
                .build();

        doReturn(response)
                .when(userApiClient)
                .tokenValidationCheck(token);

        assertThrows(AccessDeniedForThisRoleException.class,
                () -> authenticationService.checkTokenValidationForRole(token, Role.JOURNALIST));
    }

    @Test
    @DisplayName("test checkTokenValidationForRole should throw AccessDeniedForThisRoleException with expected message")
    void testCheckTokenValidationForRoleShouldThrowAccessDeniedForThisRoleExceptionWithExpectedMessage() {
        String token = "jwt";
        TokenValidationResponse expectedValue = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                .withRole(Role.JOURNALIST.name())
                .build();
        String expectedMessage = "Access Denied for role: " + Role.JOURNALIST;

        doReturn(expectedValue)
                .when(userApiClient)
                .tokenValidationCheck(token);

        Exception exception = assertThrows(AccessDeniedForThisRoleException.class,
                () -> authenticationService.checkTokenValidationForRole(token, Role.SUBSCRIBER));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("test isObjectOwnedByEmailAndRole should return true")
    void testIsObjectOwnedByEmailAndRoleShouldReturnTrue() {
        Boolean actualValue = authenticationService.isObjectOwnedByEmailAndRole(
                Role.JOURNALIST.name(), Role.JOURNALIST, "email", "email");

        assertThat(actualValue).isTrue();
    }

    @Test
    @DisplayName("test isObjectOwnedByEmailAndRole should throw UserDoesNotHavePermissionException")
    void testIsObjectOwnedByEmailAndRoleShouldThrowUserDoesNotHavePermissionException() {
        String userRole = Role.SUBSCRIBER.name();

        assertThrows(UserDoesNotHavePermissionException.class,
                () -> authenticationService.isObjectOwnedByEmailAndRole(
                        userRole, Role.SUBSCRIBER, "email", "hotEmail"));
    }

    @Test
    @DisplayName("test isObjectOwnedByEmailAndRole should throw UserDoesNotHavePermissionException with expected message")
    void testIsObjectOwnedByEmailAndRoleShouldThrowUserDoesNotHavePermissionExceptionWithExpectedMessage() {
        String userRole = Role.SUBSCRIBER.name();
        String expectedMessage = "With role " + userRole + " you can update or delete only your own news/comments";

        Exception exception = assertThrows(UserDoesNotHavePermissionException.class,
                () -> authenticationService.isObjectOwnedByEmailAndRole(
                        userRole, Role.SUBSCRIBER, "email", "hotEmail"));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}
