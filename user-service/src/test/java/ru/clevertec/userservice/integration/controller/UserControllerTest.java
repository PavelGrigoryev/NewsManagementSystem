package ru.clevertec.userservice.integration.controller;

import com.google.protobuf.util.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.userservice.dto.proto.DeleteResponse;
import ru.clevertec.userservice.dto.proto.TokenValidationResponse;
import ru.clevertec.userservice.dto.proto.UserAuthenticationRequest;
import ru.clevertec.userservice.dto.proto.UserRegisterRequest;
import ru.clevertec.userservice.dto.proto.UserResponse;
import ru.clevertec.userservice.dto.proto.UserUpdateRequest;
import ru.clevertec.userservice.integration.BaseIntegrationTest;
import ru.clevertec.userservice.util.json.TokenTxtSupplier;
import ru.clevertec.userservice.util.json.UserJsonSupplier;
import ru.clevertec.userservice.util.testbuilder.TokenValidationResponseTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserAuthenticationRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserRegisterRequestTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserResponseTestBuilder;
import ru.clevertec.userservice.util.testbuilder.UserUpdateRequestTestBuilder;

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
    private static final String BEARER = "Bearer ";

    @Nested
    class RegisterPostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() throws Exception {
            UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest()
                    .withEmail("new@email.by")
                    .build();
            String content = JsonFormat.printer().print(request);

            mockMvc.perform(post("/users/register")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstname").value(request.getFirstname()))
                    .andExpect(jsonPath("$.lastname").value(request.getLastname()))
                    .andExpect(jsonPath("$.email").value(request.getEmail()))
                    .andExpect(jsonPath("$.role").value(request.getRole()))
                    .andExpect(jsonPath("$.token").isString())
                    .andExpect(jsonPath("$.tokenExpiration").isString())
                    .andExpect(jsonPath("$.createdTime").isNotEmpty())
                    .andExpect(jsonPath("$.updatedTime").isNotEmpty());
        }

        @Test
        @DisplayName("test should return expected json and status 406 if email is occupied")
        void testShouldReturnExpectedJsonAndStatus406IfEmailIsOccupied() throws Exception {
            UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest().build();
            String content = JsonFormat.printer().print(request);
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
            UserRegisterRequest request = UserRegisterRequestTestBuilder.aUserRegisterRequest()
                    .withEmail("super@email.ru")
                    .withRole("SUPER_ADMIN")
                    .build();
            String content = JsonFormat.printer().print(request);
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
            UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest().build();
            UserResponse response = UserResponseTestBuilder.aUserResponse().build();
            String content = JsonFormat.printer().print(request);

            mockMvc.perform(post("/users/authenticate")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstname").value(response.getFirstname()))
                    .andExpect(jsonPath("$.lastname").value(response.getLastname()))
                    .andExpect(jsonPath("$.email").value(request.getEmail()))
                    .andExpect(jsonPath("$.role").value(response.getRole()))
                    .andExpect(jsonPath("$.token").isString())
                    .andExpect(jsonPath("$.tokenExpiration").isString())
                    .andExpect(jsonPath("$.createdTime").isNotEmpty())
                    .andExpect(jsonPath("$.updatedTime").isNotEmpty());
        }

        @Test
        @DisplayName("test should return expected json and status 401 if user has wrong password")
        void testShouldReturnExpectedJsonAndStatus401IfUserHasWrongPassword() throws Exception {
            UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest()
                    .withPassword("wrong").build();
            String content = JsonFormat.printer().print(request);
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
            UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest()
                    .withEmail("Bad@email.com").build();
            String content = JsonFormat.printer().print(request);
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
            UserAuthenticationRequest request = UserAuthenticationRequestTestBuilder.aUserAuthenticationRequest()
                    .withEmail("Bad email").build();
            String content = JsonFormat.printer().print(request);
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
            String json = JsonFormat.printer().print(response);

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
                    .andExpect(jsonPath("$.error_message").isNotEmpty())
                    .andExpect(jsonPath("$.error_code").value(expectedErrorCode));
        }

    }

    @Nested
    @WithMockUser
    class UpdateByTokenPutEndPointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() throws Exception {
            UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
            UserResponse response = UserResponseTestBuilder.aUserResponse().build();
            String content = JsonFormat.printer().print(request);
            String token = TokenTxtSupplier.getToken2048Request();

            mockMvc.perform(put("/users")
                            .header(AUTHORIZATION, BEARER + token)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstname").value(request.getFirstname()))
                    .andExpect(jsonPath("$.lastname").value(request.getLastname()))
                    .andExpect(jsonPath("$.email").value(response.getEmail()))
                    .andExpect(jsonPath("$.role").value(response.getRole()))
                    .andExpect(jsonPath("$.token").isString())
                    .andExpect(jsonPath("$.tokenExpiration").isString())
                    .andExpect(jsonPath("$.createdTime").isNotEmpty())
                    .andExpect(jsonPath("$.updatedTime").isNotEmpty());
        }

        @Test
        @DisplayName("test should return expected json and status 401 if SignatureException")
        void testShouldReturnExpectedJsonAndStatus401IfSignatureException() throws Exception {
            String token = TokenTxtSupplier.getBadSignatureTokenRequest();
            String json = UserJsonSupplier.getSignatureErrorResponse();
            UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
            String content = JsonFormat.printer().print(request);

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
            UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest().build();
            String content = JsonFormat.printer().print(request);

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
            UserUpdateRequest request = UserUpdateRequestTestBuilder.aUserUpdateRequest()
                    .withFirstname("Ali - Muhammed")
                    .build();
            String content = JsonFormat.printer().print(request);

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
            DeleteResponse deleteResponse = DeleteResponse.newBuilder()
                    .setMessage("User with email BruceLee@shazam.com was successfully deleted")
                    .build();
            String json = JsonFormat.printer().print(deleteResponse);
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
