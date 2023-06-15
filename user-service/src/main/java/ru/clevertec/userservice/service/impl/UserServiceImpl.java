package ru.clevertec.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchUserEmailException;
import ru.clevertec.exceptionhandlerstarter.exception.UniqueEmailException;
import ru.clevertec.userservice.dto.DeleteResponse;
import ru.clevertec.userservice.dto.TokenValidationResponse;
import ru.clevertec.userservice.dto.UserResponse;
import ru.clevertec.userservice.dto.proto.UserAuthenticationRequest;
import ru.clevertec.userservice.dto.proto.UserRegisterRequest;
import ru.clevertec.userservice.dto.proto.UserUpdateRequest;
import ru.clevertec.userservice.mapper.UserMapper;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.repository.UserRepository;
import ru.clevertec.userservice.service.JwtService;
import ru.clevertec.userservice.service.UserService;

/**
 * The UserServiceImpl class implements UserService and provides the functionality for registering, authenticating,
 * updating, and deleting users. It also includes methods for extracting and validating JWT tokens.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new {@link User} with the given information and returns a {@link UserResponse} containing the
     * user's information and a JWT token.
     *
     * @param request The {@link UserRegisterRequest} containing the user's information.
     * @return A UserResponse containing the user's information and a JWT token.
     * @throws UniqueEmailException if the email is already registered by another user.
     */
    @Override
    @Transactional
    public UserResponse register(UserRegisterRequest request) {
        User user = userMapper.fromRequest(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueEmailException("Email " + user.getEmail()
                                           + " is occupied! Another user is already registered by this email!");
        }
        String jwtToken = jwtService.generateToken(user);
        return userMapper.toResponse(user, jwtToken, jwtService.extractExpiration(jwtToken).toString());
    }

    /**
     * Authenticates a {@link User} with the given email and password and returns a {@link UserResponse} containing
     * the user's information and a JWT token.
     *
     * @param request The {@link UserAuthenticationRequest} containing the user's email and password.
     * @return A UserResponse containing the user's information and a JWT token.
     * @throws NoSuchUserEmailException if there is no user registered with the given email.
     */
    @Override
    public UserResponse authenticate(UserAuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchUserEmailException("User with email " + request.getEmail() + " is not exist"));
        String jwtToken = jwtService.generateToken(user);
        return userMapper.toResponse(user, jwtToken, jwtService.extractExpiration(jwtToken).toString());
    }

    /**
     * Validates a JWT token and returns a {@link TokenValidationResponse} containing the user's email and role.
     *
     * @param token The JWT token to validate.
     * @return A TokenValidationResponse containing the user's email and role.
     */
    @Override
    public TokenValidationResponse tokenValidationCheck(String token) {
        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);
        String roles = jwtService.extractClaim(jwt, claims -> claims.get("roles")).toString();
        String role = roles.lines()
                .map(s -> s.substring(s.indexOf("=") + 1, s.length() - 2))
                .findFirst()
                .orElse("");
        return new TokenValidationResponse(role, email);
    }


    /**
     * Updates a user's information with the given {@link UserUpdateRequest} and returns a {@link UserResponse} containing
     * the updated user's information and his JWT token.
     *
     * @param request The UserUpdateRequest containing the user's to update information.
     * @param token   The JWT token of the user to update.
     * @return A UserResponse containing the updated user's information and his JWT token.
     * @throws NoSuchUserEmailException if there is no user registered with the email in the JWT token.
     */
    @Override
    @Transactional
    public UserResponse updateByToken(UserUpdateRequest request, String token) {
        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchUserEmailException("There is no User with email " + email + " to update"));
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User updatedUser = userRepository.saveAndFlush(user);
        return userMapper.toResponse(updatedUser, jwt, jwtService.extractExpiration(jwt).toString());
    }

    /**
     * Deletes a {@link User} with the email in the given JWT token and returns a {@link DeleteResponse} indicating that
     * the user was successfully deleted.
     *
     * @param token The JWT token of the user to delete.
     * @return A DeleteResponse object indicating that the user was successfully deleted.
     * @throws NoSuchUserEmailException if there is no user registered with the email in the JWT token.
     */
    @Override
    @Transactional
    public DeleteResponse deleteByToken(String token) {
        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchUserEmailException("There is no User with email " + email + " to delete"));
        userRepository.delete(user);
        return new DeleteResponse("User with email " + email + " was successfully deleted");
    }

}
