package ru.clevertec.newsservice.integration.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.exceptionhandlerstarter.exception.AccessDeniedForThisRoleException;
import ru.clevertec.newsservice.dto.user.Role;
import ru.clevertec.newsservice.dto.user.TokenValidationResponse;
import ru.clevertec.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.newsservice.service.AuthenticationService;
import ru.clevertec.newsservice.util.testbuilder.user.TokenValidationResponseTestBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@WireMockTest(httpPort = 7070)
class AuthenticationServiceImplTest extends BaseIntegrationTest {

    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    @Test
    @DisplayName("test checkTokenValidationForRole should return expected value")
    void testCheckTokenValidationForRoleShouldReturnExpectedValue() throws JsonProcessingException {
        String token = "jwt";
        TokenValidationResponse expectedValue = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
        String json = objectMapper.writeValueAsString(expectedValue);

        stubFor(WireMock.post(urlEqualTo("/users/validate"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        TokenValidationResponse actualValue = authenticationService.checkTokenValidationForRole(token, Role.SUBSCRIBER);

        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    @DisplayName("test checkTokenValidationForRole should throw AccessDeniedForThisRoleException")
    void testCheckTokenValidationForRoleShouldThrowAccessDeniedForThisRoleException() throws JsonProcessingException {
        String token = "jwt";
        TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                .withRole(Role.SUBSCRIBER.name())
                .build();
        String json = objectMapper.writeValueAsString(response);

        stubFor(WireMock.post(urlEqualTo("/users/validate"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        assertThrows(AccessDeniedForThisRoleException.class,
                () -> authenticationService.checkTokenValidationForRole(token, Role.JOURNALIST));
    }

    @Test
    @DisplayName("test checkTokenValidationForRole should throw AccessDeniedForThisRoleException with expected message")
    void testCheckTokenValidationForRoleShouldThrowAccessDeniedForThisRoleExceptionWithExpectedMessage() throws JsonProcessingException {
        String token = "jwt";
        TokenValidationResponse expectedValue = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                .withRole(Role.JOURNALIST.name())
                .build();
        String json = objectMapper.writeValueAsString(expectedValue);
        String expectedMessage = "Access Denied for role: " + Role.JOURNALIST;

        stubFor(WireMock.post(urlEqualTo("/users/validate"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        Exception exception = assertThrows(AccessDeniedForThisRoleException.class,
                () -> authenticationService.checkTokenValidationForRole(token, Role.SUBSCRIBER));
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}
