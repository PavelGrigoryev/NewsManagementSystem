package ru.clevertec.newsservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.clevertec.newsservice.dto.user.AuthenticationRequest;
import ru.clevertec.newsservice.dto.user.RegisterRequest;
import ru.clevertec.newsservice.dto.user.RoleResponse;
import ru.clevertec.newsservice.dto.user.TokenRequest;
import ru.clevertec.newsservice.dto.user.UserResponse;

@FeignClient(name = "UserApiClient", url = "${base.url.users}")
public interface UserApiClient {

    @PostMapping("/register")
    UserResponse register(@RequestBody RegisterRequest registerRequest);

    @PostMapping("/authenticate")
    UserResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest);

    @PostMapping("/validate")
    RoleResponse tokenValidationCheck(@RequestHeader(HttpHeaders.AUTHORIZATION)
                                      TokenRequest tokenRequest);

}
