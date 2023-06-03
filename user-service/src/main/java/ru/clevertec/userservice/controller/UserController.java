package ru.clevertec.userservice.controller;

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
import ru.clevertec.userservice.dto.AuthenticationRequest;
import ru.clevertec.userservice.dto.RegisterRequest;
import ru.clevertec.userservice.dto.RoleResponse;
import ru.clevertec.userservice.dto.TokenRequest;
import ru.clevertec.userservice.dto.UserResponse;
import ru.clevertec.userservice.service.UserService;

@Slf4j
@Loggable
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @PostMapping("/validate")
    public ResponseEntity<RoleResponse> tokenValidationCheck(@RequestHeader(HttpHeaders.AUTHORIZATION)
                                                             TokenRequest tokenRequest) {
        log.warn(tokenRequest.toString());
        return ResponseEntity.ok(userService.tokenValidationCheck(tokenRequest));
    }

}
