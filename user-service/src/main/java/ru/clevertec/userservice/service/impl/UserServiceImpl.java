package ru.clevertec.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchUserEmailException;
import ru.clevertec.exceptionhandlerstarter.exception.UniqueEmailException;
import ru.clevertec.userservice.dto.AuthenticationRequest;
import ru.clevertec.userservice.dto.RegisterRequest;
import ru.clevertec.userservice.dto.RoleResponse;
import ru.clevertec.userservice.dto.TokenRequest;
import ru.clevertec.userservice.dto.UserResponse;
import ru.clevertec.userservice.mapper.UserMapper;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.repository.UserRepository;
import ru.clevertec.userservice.service.JwtService;
import ru.clevertec.userservice.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        User user = userMapper.fromRequest(registerRequest);
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

    @Override
    public UserResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new NoSuchUserEmailException("User with email " + request.email() + " is not exist"));
        String jwtToken = jwtService.generateToken(user);
        return userMapper.toResponse(user, jwtToken, jwtService.extractExpiration(jwtToken).toString());
    }

    @Override
    public RoleResponse tokenValidationCheck(TokenRequest tokenRequest) {
        String jwt = tokenRequest.jwt().substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        String role = "";
        if (userEmail != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (Boolean.TRUE.equals(jwtService.isTokenValid(jwt, userDetails))) {
                String roles = jwtService.extractClaim(jwt, claims -> claims.get("roles")).toString();
                role = roles.lines()
                        .map(s -> s.substring(s.indexOf("=") + 1, s.length() - 2))
                        .findFirst()
                        .orElse("");
                return new RoleResponse(role);
            }
        }
        return new RoleResponse(role);
    }

}
