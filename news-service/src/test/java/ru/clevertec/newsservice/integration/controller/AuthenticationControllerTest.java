package ru.clevertec.newsservice.integration.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.protobuf.util.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsservice.dto.proto.DeleteResponse;
import ru.clevertec.newsservice.dto.proto.UserAuthenticationRequest;
import ru.clevertec.newsservice.dto.proto.UserRegisterRequest;
import ru.clevertec.newsservice.dto.proto.UserResponse;
import ru.clevertec.newsservice.dto.proto.UserUpdateRequest;
import ru.clevertec.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.newsservice.util.json.AuthenticationJsonSupplier;
import ru.clevertec.newsservice.util.json.CommonErrorJsonSupplier;
import ru.clevertec.newsservice.util.testbuilder.user.UserAuthenticationRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.user.UserRegisterRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.user.UserResponseTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.user.UserUpdateRequestTestBuilder;

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

    @Test
    @DisplayName("test register should return expected json and status 201")
    void testRegisterShouldReturnExpectedJsonAndStatus201() throws Exception {
        UserResponse response = UserResponseTestBuilder.aUserResponse().build();
        UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest().build();
        String content = JsonFormat.printer().print(request);
        String json = JsonFormat.printer().print(response);

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
        UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest().build();
        String content = JsonFormat.printer().print(request);
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
        UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest().withRole("SUPER_ADMIN").build();
        String content = JsonFormat.printer().print(request);
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
        UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest().build();
        String content = JsonFormat.printer().print(request);
        String json = JsonFormat.printer().print(response);

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
        UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest()
                .withPassword("wrong").build();
        String content = JsonFormat.printer().print(request);
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
        UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest()
                .withEmail("Bad@email.com").build();
        String content = JsonFormat.printer().print(request);
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
        UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest()
                .withEmail("Bad email").build();
        String content = JsonFormat.printer().print(request);
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
        UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
        UserResponse response = UserResponseTestBuilder.aUserResponse()
                .withFirstname(request.getFirstname())
                .withLastname(request.getLastname())
                .build();
        String content = JsonFormat.printer().print(request);
        String json = JsonFormat.printer().print(response);

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
        UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
        String content = JsonFormat.printer().print(request);
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
        UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
        String content = JsonFormat.printer().print(request);
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
        UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest()
                .withFirstname("Ali - Muhammed").build();
        String content = JsonFormat.printer().print(request);
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
        DeleteResponse deleteResponse = DeleteResponse.newBuilder()
                .setMessage("User with email BruceLee@shazam.com was successfully deleted")
                .build();
        String json = JsonFormat.printer().print(deleteResponse);

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
