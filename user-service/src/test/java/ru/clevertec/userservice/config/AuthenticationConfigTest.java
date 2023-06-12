package ru.clevertec.userservice.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchUserEmailException;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.repository.UserRepository;
import ru.clevertec.userservice.util.testbuilder.UserTestBuilder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AuthenticationConfigTest {

    @InjectMocks
    private AuthenticationConfig authenticationConfig;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("test should return UserDetails with expected email")
    void testShouldReturnUserDetailsWithExpectedEmail() {
        String expectedValue = "Good@email.com";
        User user = UserTestBuilder.aUser().withEmail(expectedValue).build();

        doReturn(Optional.of(user))
                .when(userRepository)
                .findByEmail(expectedValue);

        String actualValue = authenticationConfig.userDetailsService().loadUserByUsername(expectedValue).getUsername();

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test should throw NoSuchUserEmailException with expected message")
    void testShouldThrowNoSuchUserEmailExceptionWithExpectedMessage() {
        String userEmail = "Good@email.com";
        String expectedMessage = "User with email " + userEmail + " is not exist";
        UserDetailsService userDetailsService = authenticationConfig.userDetailsService();

        Exception exception = assertThrows(NoSuchUserEmailException.class,
                () -> userDetailsService.loadUserByUsername(userEmail));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName("test should return expected instance of DaoAuthenticationProvider")
    void testShouldReturnExpectedInstanceOfDaoAuthenticationProvider() {
        Class<DaoAuthenticationProvider> expectedClass = DaoAuthenticationProvider.class;

        AuthenticationProvider actualClass = authenticationConfig.authenticationProvider();

        assertThat(actualClass).isInstanceOf(expectedClass);
    }

    @Test
    @DisplayName("test should return AuthenticationManager from AuthenticationConfiguration")
    void testShouldReturnAuthenticationManagerFromAuthenticationConfiguration() throws Exception {
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager expectedValue = mock(AuthenticationManager.class);

        doReturn(expectedValue)
                .when(configuration)
                .getAuthenticationManager();

        AuthenticationManager actualValue = authenticationConfig.authenticationManager(configuration);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test should return expected instance of BCryptPasswordEncoder")
    void testShouldReturnExpectedInstanceOfBCryptPasswordEncoder() {
        Class<BCryptPasswordEncoder> expecteClass = BCryptPasswordEncoder.class;

        PasswordEncoder actualClass = authenticationConfig.passwordEncoder();

        assertThat(actualClass).isInstanceOf(expecteClass);
    }

}
