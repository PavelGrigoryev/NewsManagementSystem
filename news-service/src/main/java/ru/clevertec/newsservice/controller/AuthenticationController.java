package ru.clevertec.newsservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.loggingstarter.annotation.Loggable;
import ru.clevertec.newsservice.client.UserApiClient;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.user.AuthenticationRequest;
import ru.clevertec.newsservice.dto.user.RegisterRequest;
import ru.clevertec.newsservice.dto.user.UpdateRequest;
import ru.clevertec.newsservice.dto.user.UserResponse;

@Loggable
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserApiClient userApiClient;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userApiClient.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(userApiClient.authenticate(request));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateByToken(@RequestBody @Valid UpdateRequest request,
                                                      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userApiClient.updateByToken(request, token));
    }

    @DeleteMapping
    public ResponseEntity<DeleteResponse> deleteByToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(userApiClient.deleteByToken(token));
    }

}
