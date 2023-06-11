package ru.clevertec.newsservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.user.AuthenticationRequest;
import ru.clevertec.newsservice.dto.user.RegisterRequest;
import ru.clevertec.newsservice.dto.user.UpdateRequest;
import ru.clevertec.newsservice.dto.user.UserResponse;
import ru.clevertec.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.newsservice.util.json.AuthenticationJsonSupplier;
import ru.clevertec.newsservice.util.json.CommonErrorJsonSupplier;
import ru.clevertec.newsservice.util.testbuilder.user.AuthenticationRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.user.RegisterRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.user.UpdateRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.user.UserResponseTestBuilder;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WireMockTest(httpPort = 7070)
@RequiredArgsConstructor
class AuthenticationControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    @DisplayName("test register should return expected json and status 201")
    void testRegisterShouldReturnExpectedJsonAndStatus201() throws Exception {
        UserResponse response = UserResponseTestBuilder.aUserResponse().build();
        RegisterRequest request = RegisterRequestTestBuilder.aRegisterRequest().build();
        String content = objectMapper.writeValueAsString(request);
        String json = objectMapper.writeValueAsString(response);

        stubFor(WireMock.post(urlEqualTo("/users/register"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        mockMvc.perform(post("/auth/register")
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test register should return expected json and status 406 if email is occupied")
    void testRegisterShouldReturnExpectedJsonAndStatus406IfEmailIsOccupied() throws Exception {
        RegisterRequest request = RegisterRequestTestBuilder.aRegisterRequest().build();
        String content = objectMapper.writeValueAsString(request);
        String json = AuthenticationJsonSupplier.getUniqueEmailErrorResponse();

        stubFor(WireMock.post(urlEqualTo("/users/register"))
                .willReturn(aResponse()
                        .withStatus(406)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        mockMvc.perform(post("/auth/register")
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test register should return expected json and status 409 if role is out of pattern")
    void testRegisterShouldReturnExpectedJsonAndStatus409IfRoleIsOutOfPattern() throws Exception {
        RegisterRequest request = RegisterRequestTestBuilder.aRegisterRequest().withRole("SUPER_ADMIN").build();
        String content = objectMapper.writeValueAsString(request);
        String json = AuthenticationJsonSupplier.getPatternRoleErrorResponse();

        mockMvc.perform(post("/auth/register")
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test authenticate should return expected json and status 200")
    void testAuthenticateShouldReturnExpectedJsonAndStatus200() throws Exception {
        UserResponse response = UserResponseTestBuilder.aUserResponse().build();
        AuthenticationRequest request = AuthenticationRequestTestBuilder.aAuthenticationRequest().build();
        String content = objectMapper.writeValueAsString(request);
        String json = objectMapper.writeValueAsString(response);

        stubFor(WireMock.post(urlEqualTo("/users/authenticate"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        mockMvc.perform(post("/auth/authenticate")
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test authenticate should return expected json and status 401 if user has wrong password")
    void testAuthenticateShouldReturnExpectedJsonAndStatus401IfUserHasWrongPassword() throws Exception {
        AuthenticationRequest request = AuthenticationRequestTestBuilder.aAuthenticationRequest()
                .withPassword("wrong").build();
        String content = objectMapper.writeValueAsString(request);
        String json = AuthenticationJsonSupplier.getWrongPasswordErrorResponse();

        stubFor(WireMock.post(urlEqualTo("/users/authenticate"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        mockMvc.perform(post("/auth/authenticate")
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test authenticate should return expected json and status 404 if user is not exist")
    void testAuthenticateShouldReturnExpectedJsonAndStatus404IfUserIsNotExist() throws Exception {
        AuthenticationRequest request = AuthenticationRequestTestBuilder.aAuthenticationRequest()
                .withEmail("Bad@email.com").build();
        String content = objectMapper.writeValueAsString(request);
        String json = AuthenticationJsonSupplier.getNotFoundErrorResponse();

        stubFor(WireMock.post(urlEqualTo("/users/authenticate"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        mockMvc.perform(post("/auth/authenticate")
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test authenticate should return expected json and status 409 if email is out of pattern")
    void testAuthenticateShouldReturnExpectedJsonAndStatus409IfEmailIsOutOfPattern() throws Exception {
        AuthenticationRequest request = AuthenticationRequestTestBuilder.aAuthenticationRequest()
                .withEmail("Bad email").build();
        String content = objectMapper.writeValueAsString(request);
        String json = AuthenticationJsonSupplier.getPatternEmailErrorResponse();

        mockMvc.perform(post("/auth/authenticate")
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test updateByToken should return expected json and status 201")
    void testUpdateByTokenShouldReturnExpectedJsonAndStatus201() throws Exception {
        UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest().build();
        UserResponse response = UserResponseTestBuilder.aUserResponse()
                .withFirstname(request.firstname())
                .withLastname(request.lastname())
                .build();
        String content = objectMapper.writeValueAsString(request);
        String json = objectMapper.writeValueAsString(response);

        stubFor(WireMock.put(urlEqualTo("/users"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        mockMvc.perform(put("/auth")
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test updateByToken should return expected json and status 401 if user has not valid token")
    void testUpdateByTokenShouldReturnExpectedJsonAndStatus401IfUserHasNotValidToken() throws Exception {
        UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest().build();
        String content = objectMapper.writeValueAsString(request);
        String json = AuthenticationJsonSupplier.getWrongPasswordErrorResponse();

        stubFor(WireMock.put(urlEqualTo("/users"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        mockMvc.perform(put("/auth")
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test updateByToken should return expected json and status 404 if user is not exist")
    void testUpdateByTokenShouldReturnExpectedJsonAndStatus404IfUserIsNotExist() throws Exception {
        UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest().build();
        String content = objectMapper.writeValueAsString(request);
        String json = AuthenticationJsonSupplier.getNotFoundErrorResponse();

        stubFor(WireMock.put(urlEqualTo("/users"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        mockMvc.perform(put("/auth")
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test updateByToken should return expected json and status 409 if firstname is out of pattern")
    void testUpdateByTokenShouldReturnExpectedJsonAndStatus409IfFirstnameIsOutOfPattern() throws Exception {
        UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest()
                .withFirstname("Али - Баба").build();
        String content = objectMapper.writeValueAsString(request);
        String json = AuthenticationJsonSupplier.getPatternFirstNameErrorResponse();

        mockMvc.perform(put("/auth")
                        .content(content)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test deleteByToken should return expected json and status 200")
    void testDeleteByTokenShouldReturnExpectedJsonAndStatus200() throws Exception {
        DeleteResponse deleteResponse = new DeleteResponse("User with email BruceLee@shazam.com was successfully deleted");
        String json = objectMapper.writeValueAsString(deleteResponse);

        stubFor(WireMock.delete(urlEqualTo("/users"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        mockMvc.perform(delete("/auth"))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test deleteByToken should return expected json and status 401 if user has not valid token")
    void testDeleteByTokenShouldReturnExpectedJsonAndStatus401IfUserHasNotValidToken() throws Exception {
        String json = CommonErrorJsonSupplier.getUnauthorizedErrorResponse();

        stubFor(WireMock.delete(urlEqualTo("/users"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        mockMvc.perform(delete("/auth"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(json));
    }

    @Test
    @DisplayName("test deleteByToken should return expected json and status 404 if user is not exist")
    void testDeleteByTokenShouldReturnExpectedJsonAndStatus404IfUserIsNotExist() throws Exception {
        String json = AuthenticationJsonSupplier.getNotFoundErrorResponse();

        stubFor(WireMock.delete(urlEqualTo("/users"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withBody(json)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

        mockMvc.perform(delete("/auth"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(json));
    }

}
