package ru.clevertec.userservice.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchUserEmailException;
import ru.clevertec.exceptionhandlerstarter.exception.UniqueEmailException;
import ru.clevertec.userservice.dto.proto.DeleteResponse;
import ru.clevertec.userservice.dto.proto.TokenValidationResponse;
import ru.clevertec.userservice.dto.proto.UserAuthenticationRequest;
import ru.clevertec.userservice.dto.proto.UserRegisterRequest;
import ru.clevertec.userservice.dto.proto.UserResponse;
import ru.clevertec.userservice.dto.proto.UserUpdateRequest;
import ru.clevertec.userservice.mapper.UserMapper;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.repository.UserRepository;
import ru.clevertec.userservice.service.JwtService;
import ru.clevertec.userservice.util.testbuilder.TokenValidationResponseTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserAuthenticationRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserRegisterRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserResponseTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserUpdateRequestTestBuilder;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Nested
    class RegisterTest {

        @Test
        @DisplayName("test should return expected UserResponse")
        void testShouldReturnExpectedUserResponse() {
            User user = UserTestBuilder.aUser().build();
            UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest().build();
            String token = "jwt";
            Date tokenExpiration = new Date();
            String password = "encoded";
            UserResponse expectedValue = UserResponseTestBuilder.aUserResponse().build();

            doReturn(user)
                    .when(userMapper)
                    .fromRequest(request);

            doReturn(password)
                    .when(passwordEncoder)
                    .encode(user.getPassword());

            doReturn(user)
                    .when(userRepository)
                    .save(user);

            doReturn(token)
                    .when(jwtService)
                    .generateToken(user);

            doReturn(tokenExpiration)
                    .when(jwtService)
                    .extractExpiration(token);

            doReturn(expectedValue)
                    .when(userMapper)
                    .toResponse(user, token, tokenExpiration.toString());

            UserResponse actualValue = userService.register(request);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw UniqueEmailException with expected message")
        void testShouldThrowUniqueEmailExceptionWithExpectedMessage() {
            User user = UserTestBuilder.aUser().build();
            UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest().build();
            String expectedMessage = "Email " + user.getEmail()
                                     + " is occupied! Another user is already registered by this email!";

            doReturn(user)
                    .when(userMapper)
                    .fromRequest(request);

            doThrow(new DataIntegrityViolationException(""))
                    .when(userRepository)
                    .save(user);

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
            User user = UserTestBuilder.aUser().build();
            String token = "jwt";
            Date tokenExpiration = new Date();
            Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            UserResponse expectedValue = UserResponseTestBuilder.aUserResponse().build();

            doReturn(authentication)
                    .when(authenticationManager)
                    .authenticate(authentication);

            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findByEmail(request.getEmail());

            doReturn(token)
                    .when(jwtService)
                    .generateToken(user);

            doReturn(tokenExpiration)
                    .when(jwtService)
                    .extractExpiration(token);

            doReturn(expectedValue)
                    .when(userMapper)
                    .toResponse(user, token, tokenExpiration.toString());

            UserResponse actualValue = userService.authenticate(request);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw BadCredentialsException with expected message")
        void testShouldThrowBadCredentialsExceptionWithExpectedMessage() {
            UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest().build();
            Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            String expectedMessage = "Bad credentials";

            doThrow(new BadCredentialsException(expectedMessage))
                    .when(authenticationManager)
                    .authenticate(authentication);

            Exception exception = assertThrows(BadCredentialsException.class, () -> userService.authenticate(request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw NoSuchUserEmailException with expected message")
        void testShouldThrowNoSuchUserEmailExceptionWithExpectedMessage() {
            UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest().build();
            String expectedMessage = "User with email " + request.getEmail() + " is not exist";

            doThrow(new NoSuchUserEmailException(expectedMessage))
                    .when(userRepository)
                    .findByEmail(request.getEmail());

            Exception exception = assertThrows(NoSuchUserEmailException.class, () -> userService.authenticate(request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    class TokenValidationCheck {

        @Test
        @DisplayName("test should return expected TokenValidationResponse")
        void testShouldReturnExpectedTokenValidationResponse() {
            String bearer = "Bearer jwt";
            String token = "jwt";
            TokenValidationResponse expectedValue = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String roles = "[{authority=ADMIN}]";

            doReturn(expectedValue.getEmail())
                    .when(jwtService)
                    .extractUsername(token);

            doReturn(roles)
                    .when(jwtService)
                    .extractClaim(any(), any());

            TokenValidationResponse actualValue = userService.tokenValidationCheck(bearer);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class UpdateByToken {

        @Test
        @DisplayName("test should return expected UserResponse")
        void testShouldReturnExpectedUserResponse() {
            String bearer = "Bearer jwt";
            String token = "jwt";
            Date tokenExpiration = new Date();
            UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
            User user = UserTestBuilder.aUser().build();
            UserResponse expectedValue = UserResponseTestBuilder.aUserResponse().build();

            doReturn(user.getEmail())
                    .when(jwtService)
                    .extractUsername(token);

            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findByEmail(user.getEmail());

            doReturn(user.getPassword())
                    .when(passwordEncoder)
                    .encode(request.getPassword());

            doReturn(user)
                    .when(userRepository)
                    .saveAndFlush(user);

            doReturn(tokenExpiration)
                    .when(jwtService)
                    .extractExpiration(token);

            doReturn(expectedValue)
                    .when(userMapper)
                    .toResponse(user, token, tokenExpiration.toString());

            UserResponse actualValue = userService.updateByToken(request, bearer);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw NoSuchUserEmailException with expected message")
        void testShouldThrowNoSuchUserEmailExceptionWithExpectedMessage() {
            String bearer = "Bearer jwt";
            String token = "jwt";
            UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
            User user = UserTestBuilder.aUser().build();
            String expectedMessage = "There is no User with email " + user.getEmail() + " to update";

            doReturn(user.getEmail())
                    .when(jwtService)
                    .extractUsername(token);

            doThrow(new NoSuchUserEmailException(expectedMessage))
                    .when(userRepository)
                    .findByEmail(user.getEmail());

            Exception exception = assertThrows(NoSuchUserEmailException.class,
                    () -> userService.updateByToken(request, bearer));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    class DeleteByToken {

        @Test
        @DisplayName("test should return expected DeleteResponse")
        void testShouldReturnExpectedDeleteResponse() {
            String bearer = "Bearer jwt";
            String token = "jwt";
            User user = UserTestBuilder.aUser().build();
            DeleteResponse expectedValue = DeleteResponse.newBuilder()
                    .setMessage("User with email " + user.getEmail() + " was successfully deleted")
                    .build();

            doReturn(user.getEmail())
                    .when(jwtService)
                    .extractUsername(token);

            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findByEmail(user.getEmail());

            doNothing()
                    .when(userRepository)
                    .delete(user);

            DeleteResponse actualValue = userService.deleteByToken(bearer);

            verify(userRepository, times(1))
                    .delete(user);
            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw NoSuchUserEmailException with expected message")
        void testShouldThrowNoSuchUserEmailExceptionWithExpectedMessage() {
            String bearer = "Bearer jwt";
            String token = "jwt";
            User user = UserTestBuilder.aUser().build();
            String expectedMessage = "There is no User with email " + user.getEmail() + " to delete";

            doReturn(user.getEmail())
                    .when(jwtService)
                    .extractUsername(token);

            doThrow(new NoSuchUserEmailException(expectedMessage))
                    .when(userRepository)
                    .findByEmail(user.getEmail());

            Exception exception = assertThrows(NoSuchUserEmailException.class, () -> userService.deleteByToken(bearer));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

}
