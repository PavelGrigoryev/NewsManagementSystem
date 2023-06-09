package ru.clevertec.newsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.loggingstarter.annotation.Loggable;
import ru.clevertec.newsservice.client.UserApiClient;
import ru.clevertec.newsservice.controller.openapi.AuthenticationOpenApi;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.user.AuthenticationRequest;
import ru.clevertec.newsservice.dto.user.RegisterRequest;
import ru.clevertec.newsservice.dto.user.UpdateRequest;
import ru.clevertec.newsservice.dto.user.UserResponse;

@Loggable
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController implements AuthenticationOpenApi {

    private final UserApiClient userApiClient;

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userApiClient.register(request));
    }

    @Override
    @PostMapping("/authenticate")
    public ResponseEntity<UserResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(userApiClient.authenticate(request));
    }

    @Override
    @PutMapping
    public ResponseEntity<UserResponse> updateByToken(@RequestBody UpdateRequest request,
                                                      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                      String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userApiClient.updateByToken(request, token));
    }

    @Override
    @DeleteMapping
    public ResponseEntity<DeleteResponse> deleteByToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                        String token) {
        return ResponseEntity.ok(userApiClient.deleteByToken(token));
    }

}
