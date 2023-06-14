package ru.clevertec.userservice.controller;

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
import ru.clevertec.userservice.controller.openapi.UserOpenApi;
import ru.clevertec.userservice.dto.UserAuthenticationRequest;
import ru.clevertec.userservice.dto.DeleteResponse;
import ru.clevertec.userservice.dto.UserRegisterRequest;
import ru.clevertec.userservice.dto.TokenValidationResponse;
import ru.clevertec.userservice.dto.UserUpdateRequest;
import ru.clevertec.userservice.dto.UserResponse;
import ru.clevertec.userservice.service.UserService;

@Loggable
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserOpenApi {

    private final UserService userService;

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(request));
    }

    @Override
    @PostMapping("/authenticate")
    public ResponseEntity<UserResponse> authenticate(@RequestBody UserAuthenticationRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @Override
    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> tokenValidationCheck(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                                        String token) {
        return ResponseEntity.ok(userService.tokenValidationCheck(token));
    }

    @Override
    @PutMapping
    public ResponseEntity<UserResponse> updateByToken(@RequestBody UserUpdateRequest request,
                                                      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                      String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateByToken(request, token));
    }

    @Override
    @DeleteMapping
    public ResponseEntity<DeleteResponse> deleteByToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                        String token) {
        return ResponseEntity.ok(userService.deleteByToken(token));
    }

}
