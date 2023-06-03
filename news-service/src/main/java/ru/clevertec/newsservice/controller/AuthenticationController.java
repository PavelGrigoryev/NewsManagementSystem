package ru.clevertec.newsservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.loggingstarter.annotation.Loggable;
import ru.clevertec.newsservice.client.UserApiClient;
import ru.clevertec.newsservice.dto.user.AuthenticationRequest;
import ru.clevertec.newsservice.dto.user.RegisterRequest;
import ru.clevertec.newsservice.dto.user.RoleResponse;
import ru.clevertec.newsservice.dto.user.TokenRequest;
import ru.clevertec.newsservice.dto.user.UserResponse;

@Slf4j
@Loggable
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserApiClient userApiClient;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userApiClient.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(userApiClient.authenticate(authenticationRequest));
    }

    @PostMapping("/validate")
    public ResponseEntity<RoleResponse> tokenValidationCheck(@RequestHeader(HttpHeaders.AUTHORIZATION)
                                                                 TokenRequest tokenRequest) {
        log.warn(tokenRequest.toString());
        return ResponseEntity.ok(userApiClient.tokenValidationCheck(tokenRequest));
    }

}
