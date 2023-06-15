package ru.clevertec.newsservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.proto.UserAuthenticationRequest;
import ru.clevertec.newsservice.dto.proto.UserRegisterRequest;
import ru.clevertec.newsservice.dto.proto.UserUpdateRequest;
import ru.clevertec.newsservice.dto.user.TokenValidationResponse;
import ru.clevertec.newsservice.dto.user.UserResponse;

@FeignClient(name = "UserApiClient", url = "${base.url.users}")
public interface UserApiClient {

    @PostMapping("/register")
    UserResponse register(@RequestBody UserRegisterRequest request);

    @PostMapping("/authenticate")
    UserResponse authenticate(@RequestBody UserAuthenticationRequest request);

    @PostMapping("/validate")
    TokenValidationResponse tokenValidationCheck(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);

    @PutMapping
    UserResponse updateByToken(@RequestBody UserUpdateRequest request,
                               @RequestHeader(HttpHeaders.AUTHORIZATION) String token);

    @DeleteMapping
    DeleteResponse deleteByToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);

}
