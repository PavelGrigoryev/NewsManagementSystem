package ru.clevertec.userservice.integration.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchUserEmailException;
import ru.clevertec.exceptionhandlerstarter.exception.UniqueEmailException;
import ru.clevertec.userservice.dto.AuthenticationRequest;
import ru.clevertec.userservice.dto.DeleteResponse;
import ru.clevertec.userservice.dto.RegisterRequest;
import ru.clevertec.userservice.dto.TokenValidationResponse;
import ru.clevertec.userservice.dto.UpdateRequest;
import ru.clevertec.userservice.dto.UserResponse;
import ru.clevertec.userservice.integration.BaseIntegrationTest;
import ru.clevertec.userservice.service.UserService;
import ru.clevertec.userservice.util.json.TokenTxtSupplier;
import ru.clevertec.userservice.util.testbuilder.AuthenticationRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.RegisterRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.TokenValidationResponseTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UpdateRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserResponseTestBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
public class UserServiceImplTest extends BaseIntegrationTest {

    private final UserService userService;
    private static final String BEARER = "Bearer ";

    @Nested
    class RegisterTest {

        @Test
        @DisplayName("test should return expected UserResponse")
        void testShouldReturnExpectedUserResponse() {
            RegisterRequest request = RegisterRequestTestBuilder.aRegisterRequest()
                    .withEmail("new@eamil.ru")
                    .build();
            UserResponse expectedValue = UserResponseTestBuilder.aUserResponse()
                    .withId(4L)
                    .withEmail(request.email())
                    .build();

            UserResponse actualValue = userService.register(request);

            assertAll(
                    () -> assertThat(actualValue.firstname()).isEqualTo(expectedValue.firstname()),
                    () -> assertThat(actualValue.lastname()).isEqualTo(expectedValue.lastname()),
                    () -> assertThat(actualValue.email()).isEqualTo(expectedValue.email()),
                    () -> assertThat(actualValue.role()).isEqualTo(expectedValue.role()),
                    () -> assertThat(actualValue.token()).isNotEmpty(),
                    () -> assertThat(actualValue.tokenExpiration()).isNotEmpty(),
                    () -> assertThat(actualValue.createdTime().getYear()).isEqualTo(LocalDateTime.now().getYear()),
                    () -> assertThat(actualValue.createdTime().getMonthValue()).isEqualTo(LocalDateTime.now().getMonthValue()),
                    () -> assertThat(actualValue.createdTime().getDayOfMonth()).isEqualTo(LocalDateTime.now().getDayOfMonth()),
                    () -> assertThat(actualValue.createdTime().getHour()).isEqualTo(LocalDateTime.now().getHour()),
                    () -> assertThat(actualValue.updatedTime().getHour()).isEqualTo(LocalDateTime.now().getHour())
            );
        }

        @Test
        @DisplayName("test should throw UniqueEmailException with expected message")
        void testShouldThrowUniqueEmailExceptionWithExpectedMessage() {
            RegisterRequest request = RegisterRequestTestBuilder.aRegisterRequest().build();
            String expectedMessage = "Email " + request.email()
                                     + " is occupied! Another user is already registered by this email!";

            Exception exception = assertThrows(UniqueEmailException.class, () -> userService.register(request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    class AuthenticateTest {

        @Test
        @DisplayName("test should return expected UserResponse")
        void testShouldReturnExpectedUserResponse() {
            AuthenticationRequest request = AuthenticationRequestTestBuilder.aAuthenticationRequest().build();
            UserResponse expectedValue = UserResponseTestBuilder.aUserResponse().build();

            UserResponse actualValue = userService.authenticate(request);

            assertAll(
                    () -> assertThat(actualValue.id()).isEqualTo(expectedValue.id()),
                    () -> assertThat(actualValue.firstname()).isEqualTo(expectedValue.firstname()),
                    () -> assertThat(actualValue.lastname()).isEqualTo(expectedValue.lastname()),
                    () -> assertThat(actualValue.email()).isEqualTo(expectedValue.email()),
                    () -> assertThat(actualValue.role()).isEqualTo(expectedValue.role()),
                    () -> assertThat(actualValue.token()).isNotEmpty(),
                    () -> assertThat(actualValue.tokenExpiration()).isNotEmpty(),
                    () -> assertThat(actualValue.createdTime()).isNotNull(),
                    () -> assertThat(actualValue.updatedTime()).isNotNull()
            );
        }

        @Test
        @DisplayName("test should throw BadCredentialsException with expected message")
        void testShouldThrowBadCredentialsExceptionWithExpectedMessage() {
            AuthenticationRequest request = AuthenticationRequestTestBuilder.aAuthenticationRequest()
                    .withPassword("Bad pass").build();
            String expectedMessage = "Bad credentials";

            Exception exception = assertThrows(BadCredentialsException.class, () -> userService.authenticate(request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw InternalAuthenticationServiceException with expected message")
        void testShouldThrowInternalAuthenticationServiceExceptionWithExpectedMessage() {
            AuthenticationRequest request = AuthenticationRequestTestBuilder.aAuthenticationRequest()
                    .withEmail("NotExist@email.by").build();
            String expectedMessage = "User with email " + request.email() + " is not exist";

            Exception exception = assertThrows(InternalAuthenticationServiceException.class,
                    () -> userService.authenticate(request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    class TokenValidationCheck {

        @Test
        @DisplayName("test should return expected TokenValidationResponse")
        void testShouldReturnExpectedTokenValidationResponse() throws IOException {
            String token = TokenTxtSupplier.getToken2048Request();
            TokenValidationResponse expectedValue = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withEmail("BruceLee@shazam.com").build();

            TokenValidationResponse actualValue = userService.tokenValidationCheck(BEARER + token);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw SignatureException with expected message")
        void testShouldThrowSignatureExceptionWithExpectedMessage() throws IOException {
            String token = TokenTxtSupplier.getBadSignatureTokenRequest();
            String expectedMessage = "JWT signature does not match locally computed signature." +
                                     " JWT validity cannot be asserted and should not be trusted.";

            Exception exception = assertThrows(SignatureException.class,
                    () -> userService.tokenValidationCheck(BEARER + token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw ExpiredJwtException with expected message")
        void testShouldThrowExpiredJwtExceptionWithExpectedMessage() throws IOException {
            String token = TokenTxtSupplier.getExpiredTokenRequest();
            String expectedMessage = "JWT expired at";

            Exception exception = assertThrows(ExpiredJwtException.class,
                    () -> userService.tokenValidationCheck(BEARER + token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).startsWith(expectedMessage);
        }

    }

    @Nested
    class UpdateByToken {

        @Test
        @DisplayName("test should return expected UserResponse")
        void testShouldReturnExpectedUserResponse() throws IOException {
            String token = TokenTxtSupplier.getToken2048Request();
            UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest().build();
            UserResponse expectedValue = UserResponseTestBuilder.aUserResponse()
                    .withFirstname(request.firstname())
                    .withLastname(request.lastname())
                    .withUpdatedTime(LocalDateTime.now())
                    .build();

            UserResponse actualValue = userService.updateByToken(request, BEARER + token);

            assertAll(
                    () -> assertThat(actualValue.id()).isEqualTo(expectedValue.id()),
                    () -> assertThat(actualValue.firstname()).isEqualTo(expectedValue.firstname()),
                    () -> assertThat(actualValue.lastname()).isEqualTo(expectedValue.lastname()),
                    () -> assertThat(actualValue.email()).isEqualTo(expectedValue.email()),
                    () -> assertThat(actualValue.role()).isEqualTo(expectedValue.role()),
                    () -> assertThat(actualValue.token()).isNotEmpty(),
                    () -> assertThat(actualValue.tokenExpiration()).isNotEmpty(),
                    () -> assertThat(actualValue.createdTime()).isNotNull(),
                    () -> assertThat(actualValue.updatedTime().getYear()).isEqualTo(LocalDateTime.now().getYear()),
                    () -> assertThat(actualValue.updatedTime().getMonthValue()).isEqualTo(LocalDateTime.now().getMonthValue()),
                    () -> assertThat(actualValue.updatedTime().getDayOfMonth()).isEqualTo(LocalDateTime.now().getDayOfMonth()),
                    () -> assertThat(actualValue.updatedTime().getHour()).isEqualTo(LocalDateTime.now().getHour())
            );
        }

        @Test
        @DisplayName("test should throw NoSuchUserEmailException with expected message")
        void testShouldThrowNoSuchUserEmailExceptionWithExpectedMessage() throws IOException {
            String token = TokenTxtSupplier.getDeletedEmailTokenRequest();
            UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest().build();
            String expectedMessage = "There is no User with email Bad@email.com to update";

            Exception exception = assertThrows(NoSuchUserEmailException.class,
                    () -> userService.updateByToken(request, BEARER + token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw SignatureException with expected message")
        void testShouldThrowSignatureExceptionWithExpectedMessage() throws IOException {
            String token = TokenTxtSupplier.getBadSignatureTokenRequest();
            UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest().build();
            String expectedMessage = "JWT signature does not match locally computed signature." +
                                     " JWT validity cannot be asserted and should not be trusted.";

            Exception exception = assertThrows(SignatureException.class,
                    () -> userService.updateByToken(request, BEARER + token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw ExpiredJwtException with expected message")
        void testShouldThrowExpiredJwtExceptionWithExpectedMessage() throws IOException {
            String token = TokenTxtSupplier.getExpiredTokenRequest();
            UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest().build();
            String expectedMessage = "JWT expired at";

            Exception exception = assertThrows(ExpiredJwtException.class,
                    () -> userService.updateByToken(request, BEARER + token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).startsWith(expectedMessage);
        }

    }

    @Nested
    class DeleteByToken {

        @Test
        @DisplayName("test should return expected DeleteResponse")
        void testShouldReturnExpectedDeleteResponse() throws IOException {
            String token = TokenTxtSupplier.getToken2048Request();
            DeleteResponse expectedValue = new DeleteResponse("User with email BruceLee@shazam.com was successfully deleted");

            DeleteResponse actualValue = userService.deleteByToken(BEARER + token);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw NoSuchUserEmailException with expected message")
        void testShouldThrowNoSuchUserEmailExceptionWithExpectedMessage() throws IOException {
            String token = TokenTxtSupplier.getDeletedEmailTokenRequest();
            String expectedMessage = "There is no User with email Bad@email.com to delete";

            Exception exception = assertThrows(NoSuchUserEmailException.class,
                    () -> userService.deleteByToken(BEARER + token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw SignatureException with expected message")
        void testShouldThrowSignatureExceptionWithExpectedMessage() throws IOException {
            String token = TokenTxtSupplier.getBadSignatureTokenRequest();
            String expectedMessage = "JWT signature does not match locally computed signature." +
                                     " JWT validity cannot be asserted and should not be trusted.";

            Exception exception = assertThrows(SignatureException.class,
                    () -> userService.deleteByToken(BEARER + token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw ExpiredJwtException with expected message")
        void testShouldThrowExpiredJwtExceptionWithExpectedMessage() throws IOException {
            String token = TokenTxtSupplier.getExpiredTokenRequest();
            String expectedMessage = "JWT expired at";

            Exception exception = assertThrows(ExpiredJwtException.class,
                    () -> userService.deleteByToken(BEARER + token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).startsWith(expectedMessage);
        }

    }

}
