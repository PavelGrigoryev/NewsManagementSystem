package ru.clevertec.userservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchUserEmailException;
import ru.clevertec.userservice.repository.UserRepository;

/**
 * Configuration class for authentication related beans.
 */
@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserRepository userRepository;

    /**
     * Returns {@link UserDetailsService} that retrieves user details from the UserRepository.
     *
     * @return UserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userEmail -> userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchUserEmailException("User with email " + userEmail + " is not exist"));
    }

    /**
     * Returns {@link AuthenticationProvider} that uses the UserDetailsService and password encoder.
     *
     * @return AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Returns {@link AuthenticationManager} using the provided AuthenticationConfiguration.
     *
     * @param configuration AuthenticationConfiguration.
     * @return AuthenticationManager.
     * @throws Exception if exception occurs while creating the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Returns {@link BCryptPasswordEncoder} for encoding passwords.
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
