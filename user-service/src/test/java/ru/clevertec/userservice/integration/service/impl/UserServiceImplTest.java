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
import ru.clevertec.userservice.dto.proto.DeleteResponse;
import ru.clevertec.userservice.dto.proto.TokenValidationResponse;
import ru.clevertec.userservice.dto.proto.UserAuthenticationRequest;
import ru.clevertec.userservice.dto.proto.UserRegisterRequest;
import ru.clevertec.userservice.dto.proto.UserResponse;
import ru.clevertec.userservice.dto.proto.UserUpdateRequest;
import ru.clevertec.userservice.integration.BaseIntegrationTest;
import ru.clevertec.userservice.service.UserService;
import ru.clevertec.userservice.util.json.TokenTxtSupplier;
import ru.clevertec.userservice.util.testbuilder.TokenValidationResponseTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserAuthenticationRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserRegisterRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserResponseTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserUpdateRequestTestBuilder;

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
            UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest()
                    .withEmail("new@eamil.ru")
                    .build();
            UserResponse expectedValue = UserResponseTestBuilder.aUserResponse()
                    .withId(4L)
                    .withEmail(request.getEmail())
                    .build();

            UserResponse actualValue = userService.register(request);

            assertAll(
                    () -> assertThat(actualValue.getFirstname()).isEqualTo(expectedValue.getFirstname()),
                    () -> assertThat(actualValue.getLastname()).isEqualTo(expectedValue.getLastname()),
                    () -> assertThat(actualValue.getEmail()).isEqualTo(expectedValue.getEmail()),
                    () -> assertThat(actualValue.getRole()).isEqualTo(expectedValue.getRole()),
                    () -> assertThat(actualValue.getToken()).isNotBlank(),
                    () -> assertThat(actualValue.getTokenExpiration()).isNotBlank(),
                    () -> assertThat(actualValue.getCreatedTime()).isNotBlank()
            );
        }

        @Test
        @DisplayName("test should throw UniqueEmailException with expected message")
        void testShouldThrowUniqueEmailExceptionWithExpectedMessage() {
            UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest().build();
            String expectedMessage = "Email " + request.getEmail()
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
            UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest().build();
            UserResponse expectedValue = UserResponseTestBuilder.aUserResponse().build();

            UserResponse actualValue = userService.authenticate(request);

            assertAll(
                    () -> assertThat(actualValue.getId()).isEqualTo(expectedValue.getId()),
                    () -> assertThat(actualValue.getFirstname()).isEqualTo(expectedValue.getFirstname()),
                    () -> assertThat(actualValue.getLastname()).isEqualTo(expectedValue.getLastname()),
                    () -> assertThat(actualValue.getEmail()).isEqualTo(expectedValue.getEmail()),
                    () -> assertThat(actualValue.getRole()).isEqualTo(expectedValue.getRole()),
                    () -> assertThat(actualValue.getToken()).isNotBlank(),
                    () -> assertThat(actualValue.getTokenExpiration()).isNotBlank(),
                    () -> assertThat(actualValue.getCreatedTime()).isNotBlank(),
                    () -> assertThat(actualValue.getUpdatedTime()).isNotBlank()
            );
        }

        @Test
        @DisplayName("test should throw BadCredentialsException with expected message")
        void testShouldThrowBadCredentialsExceptionWithExpectedMessage() {
            UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest()
                    .withPassword("Bad pass").build();
            String expectedMessage = "Bad credentials";

            Exception exception = assertThrows(BadCredentialsException.class, () -> userService.authenticate(request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw InternalAuthenticationServiceException with expected message")
        void testShouldThrowInternalAuthenticationServiceExceptionWithExpectedMessage() {
            UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest()
                    .withEmail("NotExist@email.by").build();
            String expectedMessage = "User with email " + request.getEmail() + " is not exist";

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
            UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
            UserResponse expectedValue = UserResponseTestBuilder.aUserResponse()
                    .withFirstname(request.getFirstname())
                    .withLastname(request.getLastname())
                    .withUpdatedTime(LocalDateTime.now())
                    .build();

            UserResponse actualValue = userService.updateByToken(request, BEARER + token);

            assertAll(
                    () -> assertThat(actualValue.getId()).isEqualTo(expectedValue.getId()),
                    () -> assertThat(actualValue.getFirstname()).isEqualTo(expectedValue.getFirstname()),
                    () -> assertThat(actualValue.getLastname()).isEqualTo(expectedValue.getLastname()),
                    () -> assertThat(actualValue.getEmail()).isEqualTo(expectedValue.getEmail()),
                    () -> assertThat(actualValue.getRole()).isEqualTo(expectedValue.getRole()),
                    () -> assertThat(actualValue.getToken()).isNotBlank(),
                    () -> assertThat(actualValue.getTokenExpiration()).isNotBlank(),
                    () -> assertThat(actualValue.getCreatedTime()).isNotBlank(),
                    () -> assertThat(actualValue.getUpdatedTime()).isNotBlank()
            );
        }

        @Test
        @DisplayName("test should throw NoSuchUserEmailException with expected message")
        void testShouldThrowNoSuchUserEmailExceptionWithExpectedMessage() throws IOException {
            String token = TokenTxtSupplier.getDeletedEmailTokenRequest();
            UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
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
            UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
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
            UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
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
            DeleteResponse expectedValue = DeleteResponse.newBuilder()
                    .setMessage("User with email BruceLee@shazam.com was successfully deleted")
                    .build();

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
