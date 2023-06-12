package ru.clevertec.userservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.userservice.dto.AuthenticationRequest;
import ru.clevertec.userservice.dto.DeleteResponse;
import ru.clevertec.userservice.dto.RegisterRequest;
import ru.clevertec.userservice.dto.TokenValidationResponse;
import ru.clevertec.userservice.dto.UpdateRequest;
import ru.clevertec.userservice.dto.UserResponse;
import ru.clevertec.userservice.integration.BaseIntegrationTest;
import ru.clevertec.userservice.util.json.UserJsonSupplier;
import ru.clevertec.userservice.util.json.TokenTxtSupplier;
import ru.clevertec.userservice.util.testbuilder.AuthenticationRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.RegisterRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.TokenValidationResponseTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UpdateRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserResponseTestBuilder;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private static final String BEARER = "Bearer ";

    @Nested
    class RegisterPostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() throws Exception {
            RegisterRequest request = RegisterRequestTestBuilder.aRegisterRequest()
                    .withEmail("new@email.by")
                    .build();
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/users/register")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstname").value(request.firstname()))
                    .andExpect(jsonPath("$.lastname").value(request.lastname()))
                    .andExpect(jsonPath("$.email").value(request.email()))
                    .andExpect(jsonPath("$.role").value(request.role()))
                    .andExpect(jsonPath("$.token").isString())
                    .andExpect(jsonPath("$.token_expiration").isString())
                    .andExpect(jsonPath("$.created_time").isNotEmpty())
                    .andExpect(jsonPath("$.updated_time").isNotEmpty());
        }

        @Test
        @DisplayName("test should return expected json and status 406 if email is occupied")
        void testShouldReturnExpectedJsonAndStatus406IfEmailIsOccupied() throws Exception {
            RegisterRequest request = RegisterRequestTestBuilder.aRegisterRequest().build();
            String content = objectMapper.writeValueAsString(request);
            String json = UserJsonSupplier.getUniqueEmailErrorResponse();

            mockMvc.perform(post("/users/register")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if role is out of pattern")
        void testShouldReturnExpectedJsonAndStatus409IfRoleIsOutOfPattern() throws Exception {
            RegisterRequest request = RegisterRequestTestBuilder.aRegisterRequest()
                    .withEmail("super@email.ru")
                    .withRole("SUPER_ADMIN")
                    .build();
            String content = objectMapper.writeValueAsString(request);
            String json = UserJsonSupplier.getPatternRoleErrorResponse();

            mockMvc.perform(post("/users/register")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class AuthenticatePostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            AuthenticationRequest request = AuthenticationRequestTestBuilder.aAuthenticationRequest().build();
            UserResponse response = UserResponseTestBuilder.aUserResponse().build();
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/users/authenticate")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstname").value(response.firstname()))
                    .andExpect(jsonPath("$.lastname").value(response.lastname()))
                    .andExpect(jsonPath("$.email").value(request.email()))
                    .andExpect(jsonPath("$.role").value(response.role().name()))
                    .andExpect(jsonPath("$.token").isString())
                    .andExpect(jsonPath("$.token_expiration").isString())
                    .andExpect(jsonPath("$.created_time").isNotEmpty())
                    .andExpect(jsonPath("$.updated_time").isNotEmpty());
        }

        @Test
        @DisplayName("test should return expected json and status 401 if user has wrong password")
        void testShouldReturnExpectedJsonAndStatus401IfUserHasWrongPassword() throws Exception {
            AuthenticationRequest request = AuthenticationRequestTestBuilder.aAuthenticationRequest()
                    .withPassword("wrong").build();
            String content = objectMapper.writeValueAsString(request);
            String json = UserJsonSupplier.getWrongPasswordErrorResponse();

            mockMvc.perform(post("/users/authenticate")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if user is not exist")
        void testShouldReturnExpectedJsonAndStatus404IfUserIsNotExist() throws Exception {
            AuthenticationRequest request = AuthenticationRequestTestBuilder.aAuthenticationRequest()
                    .withEmail("Bad@email.com").build();
            String content = objectMapper.writeValueAsString(request);
            String json = UserJsonSupplier.getNotFoundErrorResponse();

            mockMvc.perform(post("/users/authenticate")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if email is out of pattern")
        void testShouldReturnExpectedJsonAndStatus409IfEmailIsOutOfPattern() throws Exception {
            AuthenticationRequest request = AuthenticationRequestTestBuilder.aAuthenticationRequest()
                    .withEmail("Bad email").build();
            String content = objectMapper.writeValueAsString(request);
            String json = UserJsonSupplier.getPatternEmailErrorResponse();

            mockMvc.perform(post("/users/authenticate")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    @WithMockUser
    class TokenValidationCheckPostEndPointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withEmail("BruceLee@shazam.com")
                    .build();
            String token = TokenTxtSupplier.getToken2048Request();
            String json = objectMapper.writeValueAsString(response);

            mockMvc.perform(post("/users/validate")
                            .header(AUTHORIZATION, BEARER + token))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 401 if MalformedJwtException")
        void testShouldReturnExpectedJsonAndStatus401IfMalformedJwtException() throws Exception {
            String token = "Bad Token";
            String json = UserJsonSupplier.getMalformedErrorResponse();

            mockMvc.perform(post("/users/validate")
                            .header(AUTHORIZATION, BEARER + token))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 401 if SignatureException")
        void testShouldReturnExpectedJsonAndStatus401IfSignatureException() throws Exception {
            String token = TokenTxtSupplier.getBadSignatureTokenRequest();
            String json = UserJsonSupplier.getSignatureErrorResponse();

            mockMvc.perform(post("/users/validate")
                            .header(AUTHORIZATION, BEARER + token))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 401 if ExpiredJwtException")
        void testShouldReturnExpectedJsonAndStatus401IfExpiredJwtException() throws Exception {
            String expectedException = "ExpiredJwtException";
            String expectedErrorCode = "401 UNAUTHORIZED";
            String token = TokenTxtSupplier.getExpiredTokenRequest();

            mockMvc.perform(post("/users/validate")
                            .header(AUTHORIZATION, BEARER + token))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.exception").value(expectedException))
                    .andExpect(jsonPath("$.errorMessage").isNotEmpty())
                    .andExpect(jsonPath("$.errorCode").value(expectedErrorCode));
        }

    }

    @Nested
    @WithMockUser
    class UpdateByTokenPutEndPointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() throws Exception {
            UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest().build();
            UserResponse response = UserResponseTestBuilder.aUserResponse().build();
            String content = objectMapper.writeValueAsString(request);
            String token = TokenTxtSupplier.getToken2048Request();

            mockMvc.perform(put("/users")
                            .header(AUTHORIZATION, BEARER + token)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstname").value(request.firstname()))
                    .andExpect(jsonPath("$.lastname").value(request.lastname()))
                    .andExpect(jsonPath("$.email").value(response.email()))
                    .andExpect(jsonPath("$.role").value(response.role().name()))
                    .andExpect(jsonPath("$.token").isString())
                    .andExpect(jsonPath("$.token_expiration").isString())
                    .andExpect(jsonPath("$.created_time").isNotEmpty())
                    .andExpect(jsonPath("$.updated_time").isNotEmpty());
        }

        @Test
        @DisplayName("test should return expected json and status 401 if SignatureException")
        void testShouldReturnExpectedJsonAndStatus401IfSignatureException() throws Exception {
            String token = TokenTxtSupplier.getBadSignatureTokenRequest();
            String json = UserJsonSupplier.getSignatureErrorResponse();
            UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest().build();
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/users")
                            .header(AUTHORIZATION, BEARER + token)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if user is not exist")
        void testShouldReturnExpectedJsonAndStatus404IfUserIsNotExist() throws Exception {
            String token = TokenTxtSupplier.getDeletedEmailTokenRequest();
            String json = UserJsonSupplier.getNotFoundUpdateErrorResponse();
            UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest().build();
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/users")
                            .header(AUTHORIZATION, BEARER + token)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if firstname is out of pattern")
        void testShouldReturnExpectedJsonAndStatus409IfFirstnameIsOutOfPattern() throws Exception {
            String token = TokenTxtSupplier.getToken2048Request();
            String json = UserJsonSupplier.getPatternFirstnameErrorResponse();
            UpdateRequest request = UpdateRequestTestBuilder.aUpdateRequest()
                    .withFirstname("Али - Баба")
                    .build();
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/users")
                            .header(AUTHORIZATION, BEARER + token)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    @WithMockUser
    class DeleteByTokenDeleteEndPointTes {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            DeleteResponse deleteResponse = new DeleteResponse("User with email BruceLee@shazam.com was successfully deleted");
            String json = objectMapper.writeValueAsString(deleteResponse);
            String token = TokenTxtSupplier.getToken2048Request();

            mockMvc.perform(delete("/users")
                            .header(AUTHORIZATION, BEARER + token))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 401 if user has not valid token")
        void testShouldReturnExpectedJsonAndStatus401IfUserHasNotValidToken() throws Exception {
            String json = UserJsonSupplier.getSignatureErrorResponse();
            String token = TokenTxtSupplier.getBadSignatureTokenRequest();

            mockMvc.perform(delete("/users")
                            .header(AUTHORIZATION, BEARER + token))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if user is not exist")
        void testShouldReturnExpectedJsonAndStatus404IfUserIsNotExist() throws Exception {
            String json = UserJsonSupplier.getNotFoundDeleteErrorResponse();
            String token = TokenTxtSupplier.getDeletedEmailTokenRequest();

            mockMvc.perform(delete("/users")
                            .header(AUTHORIZATION, BEARER + token))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

    }

}
