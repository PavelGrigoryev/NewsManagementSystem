package ru.clevertec.newsservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.google.protobuf.util.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.proto.CommentRequest;
import ru.clevertec.newsservice.dto.proto.CommentWithNewsRequest;
import ru.clevertec.newsservice.dto.user.Role;
import ru.clevertec.newsservice.dto.user.TokenValidationResponse;
import ru.clevertec.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.newsservice.util.json.CommentJsonSupplier;
import ru.clevertec.newsservice.util.json.CommonErrorJsonSupplier;
import ru.clevertec.newsservice.util.json.NewsJsonSupplier;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentWithNewsRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.user.TokenValidationResponseTestBuilder;

import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WireMockTest(httpPort = 7070)
@RequiredArgsConstructor
class CommentControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            long id = 5;
            String json = CommentJsonSupplier.getCommentResponse();

            mockMvc.perform(get("/comments/" + id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 256;
            String json = CommentJsonSupplier.getNotFoundGetCommentResponse();

            mockMvc.perform(get("/comments/" + wrongId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if id is not positive")
        void testShouldReturnExpectedJsonAndStatus409IfIsNotPositive() throws Exception {
            long wrongId = -1L;
            String json = CommonErrorJsonSupplier.getPositiveErrorGetResponse();

            mockMvc.perform(get("/comments/" + wrongId))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class FindNewsByNewsIdWithCommentsGetEndpointTest {

        @Test
        @DisplayName("test should return news json and status 200 with no comments on the page")
        void testShouldReturnNewsJsonAndStatus200WithNoCommentsOnThePage() throws Exception {
            long newsId = 5;
            int page = 5;

            mockMvc.perform(get("/comments/news/" + newsId + "?page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.comments").isEmpty());
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            long newsId = 5;
            int page = 0;
            String json = CommentJsonSupplier.getNewsWithCommentsResponse();

            mockMvc.perform(get("/comments/news/" + newsId + "?page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongNewsId = 122;
            int page = 0;
            String json = NewsJsonSupplier.getNotFoundGetNewsResponse();

            mockMvc.perform(get("/comments/news/" + wrongNewsId + "?page=" + page))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 406 if typo in sort")
        void testShouldReturnExpectedJsonAndStatus406IfTypoInSort() throws Exception {
            long newsId = 5;
            int page = 0;
            String typoInSort = "usename";
            String json = CommentJsonSupplier.getTypoInSortCommentResponse();

            mockMvc.perform(get("/comments/news/" + newsId + "?page=" + page + "&sort=" + typoInSort))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if id is not positive")
        void testShouldReturnExpectedJsonAndStatus409IfIsNotPositive() throws Exception {
            long wrongNewsId = -1;
            int page = 0;
            String json = CommonErrorJsonSupplier.getPositiveErrorGetAllResponse();

            mockMvc.perform(get("/comments/news/" + wrongNewsId + "?page=" + page))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class FindAllByMatchingTextParamsGetEndpointTest {

        @Test
        @DisplayName("test should return empty json and status 200 if there is no comments on the page")
        void testShouldReturnEmptyJsonAndStatus200IfThereIsNoNewsOnThePage() throws Exception {
            int page = 5;
            String username = "Soccer";
            String json = "[]";

            mockMvc.perform(get("/comments/params?username=" + username + "&page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            int page = 0;
            String username = "Soccer";
            String json = CommentJsonSupplier.getMatcherCommentResponse();

            mockMvc.perform(get("/comments/params?username=" + username + "&page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 406 if typo in sort")
        void testShouldReturnExpectedJsonAndStatus406IfTypoInSort() throws Exception {
            int page = 0;
            String username = "Soccer";
            String typoInSort = "usename";
            String json = CommentJsonSupplier.getTypoInSortCommentResponse();

            mockMvc.perform(get("/comments/params?username=" + username + "&page=" + page + "&sort=" + typoInSort))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    @WireMockTest(httpPort = 7070)
    class SavePostEndpointTest {

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test should return expected json and status 201 for Subscriber and Admin")
        @MethodSource("ru.clevertec.newsservice.integration.controller.CommentControllerTest#getArgumentsForPostTest")
        void testShouldReturnExpectedJsonAndStatus201ForSubscriberAndAdmin(Long expectedId,
                                                                           TokenValidationResponse response) throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            String content = JsonFormat.printer().print(request);
            String json = objectMapper.writeValueAsString(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(expectedId))
                    .andExpect(jsonPath("$.text").value(request.getText()))
                    .andExpect(jsonPath("$.username").value(request.getUsername()))
                    .andExpect(jsonPath("$.email").value(response.email()));
        }

        @Test
        @DisplayName("test should return expected json and status 401 if user is unauthorized")
        void testShouldReturnExpectedJsonAndStatus401IfUserIsUnauthorized() throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            String content = JsonFormat.printer().print(request);
            String json = CommonErrorJsonSupplier.getUnauthorizedErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(401)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 403 for Journalist")
        void testShouldReturnExpectedJsonAndStatus403ForJournalist() throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            String content = JsonFormat.printer().print(request);
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.JOURNALIST.name()).build();
            String json = objectMapper.writeValueAsString(response);
            String expectedJson = CommonErrorJsonSupplier.getForbiddenForJournalistErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest()
                    .withNewsId(122L).build();
            String content = JsonFormat.printer().print(request);
            String expectedJson = NewsJsonSupplier.getNotFoundGetNewsResponse();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name()).build();
            String json = objectMapper.writeValueAsString(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if username is out of pattern")
        void testShouldReturnExpectedJsonAndStatus409IfUserNameIsOutOfPattern() throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest()
                    .withUsername("Ali - Muhammed")
                    .build();
            String content = JsonFormat.printer().print(request);
            String json = CommentJsonSupplier.getPatternCommentWithNewsErrorResponse();

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    @WireMockTest(httpPort = 7070)
    class UpdateByIdPutEndpointTest {

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test should return expected json and status 201 for Subscriber and Admin")
        @MethodSource("ru.clevertec.newsservice.integration.controller.CommentControllerTest#getArgumentsForPutTest")
        void testShouldReturnExpectedJsonAndStatus201ForSubscriberAndAdmin(Long expectedId,
                                                                           TokenValidationResponse response) throws Exception {
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            String content = JsonFormat.printer().print(request);
            String json = objectMapper.writeValueAsString(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(put("/comments/" + expectedId)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(expectedId))
                    .andExpect(jsonPath("$.text").value(request.getText()))
                    .andExpect(jsonPath("$.username").value(request.getUsername()))
                    .andExpect(jsonPath("$.email").value(response.email()));
        }

        @Test
        @DisplayName("test should return expected json and status 401 if user is unauthorized")
        void testShouldReturnExpectedJsonAndStatus401IfUserIsUnauthorized() throws Exception {
            long id = 3L;
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            String content = JsonFormat.printer().print(request);
            String json = CommonErrorJsonSupplier.getUnauthorizedErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(401)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(put("/comments/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 403 for Journalist")
        void testShouldReturnExpectedJsonAndStatus403ForJournalist() throws Exception {
            long id = 2L;
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            String content = JsonFormat.printer().print(request);
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.JOURNALIST.name()).build();
            String json = objectMapper.writeValueAsString(response);
            String expectedJson = CommonErrorJsonSupplier.getForbiddenForJournalistErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(put("/comments/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 122;
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            String content = JsonFormat.printer().print(request);
            String expectedJson = CommentJsonSupplier.getNotFoundPutCommentResponse();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(put("/comments/" + wrongId)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 405 for Subscriber with wrong permission")
        void testShouldReturnExpectedJsonAndStatus405ForSubscriberWithWrongPermission() throws Exception {
            long id = 2L;
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            String content = JsonFormat.printer().print(request);
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name()).build();
            String json = objectMapper.writeValueAsString(response);
            String expectedJson = CommonErrorJsonSupplier.getMethodNotAllowedForSubscriberErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(put("/comments/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isMethodNotAllowed())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if username is out of pattern")
        void testShouldReturnExpectedJsonAndStatus409IfUsernameIsOutOfPattern() throws Exception {
            long id = 5;
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().withUsername("Ali - Muhammed").build();
            String content = JsonFormat.printer().print(request);
            String json = CommentJsonSupplier.getPatternCommentErrorResponse();

            mockMvc.perform(put("/comments/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if comment id is less then 1")
        void testShouldReturnExpectedJsonAndStatus409IfCommentIdIsLessThenOne() throws Exception {
            long id = 0;
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            String content = JsonFormat.printer().print(request);
            String json = CommonErrorJsonSupplier.getIdErrorResponse();

            mockMvc.perform(put("/comments/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    @WireMockTest(httpPort = 7070)
    class DeleteByIdEndpointTest {

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test should return expected json and status 200 for Subscriber and Admin")
        @MethodSource("ru.clevertec.newsservice.integration.controller.CommentControllerTest#getArgumentsForDeleteTest")
        void testShouldReturnExpectedJsonAndStatus200ForSubscriberAndAdmin(Long id,
                                                                           TokenValidationResponse response) throws Exception {
            DeleteResponse deleteResponse = new DeleteResponse("Comment with ID " + id + " was successfully deleted");
            String expectedJson = objectMapper.writeValueAsString(deleteResponse);
            String json = objectMapper.writeValueAsString(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(delete("/comments/" + id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 401 if user is unauthorized")
        void testShouldReturnExpectedJsonAndStatus401IfUserIsUnauthorized() throws Exception {
            long id = 3L;
            String json = CommonErrorJsonSupplier.getUnauthorizedErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(401)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(delete("/comments/" + id))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 403 for Journalist")
        void testShouldReturnExpectedJsonAndStatus403ForJournalist() throws Exception {
            long id = 2L;
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.JOURNALIST.name()).build();
            String json = objectMapper.writeValueAsString(response);
            String expectedJson = CommonErrorJsonSupplier.getForbiddenForJournalistErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(delete("/comments/" + id))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 122;
            String expectedJson = CommentJsonSupplier.getNotFoundDeleteCommentResponse();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(delete("/comments/" + wrongId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 405 for Subscriber with wrong permission")
        void testShouldReturnExpectedJsonAndStatus405ForSubscriberWithWrongPermission() throws Exception {
            long id = 2L;
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name()).build();
            String json = objectMapper.writeValueAsString(response);
            String expectedJson = CommonErrorJsonSupplier.getMethodNotAllowedForSubscriberErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(delete("/comments/" + id))
                    .andExpect(status().isMethodNotAllowed())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if id is not positive")
        void testShouldReturnExpectedJsonAndStatus409IfIsNotPositive() throws Exception {
            long wrongId = -1L;
            String json = CommonErrorJsonSupplier.getPositiveErrorDeleteResponse();

            mockMvc.perform(delete("/comments/" + wrongId))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    private static Stream<Arguments> getArgumentsForPostTest() {
        return Stream.of(
                Arguments.of(26L, TokenValidationResponseTestBuilder.aTokenValidationResponse().build()),
                Arguments.of(27L, TokenValidationResponseTestBuilder.aTokenValidationResponse()
                        .withRole(Role.SUBSCRIBER.name()).build()));
    }

    private static Stream<Arguments> getArgumentsForPutTest() {
        return Stream.of(
                Arguments.of(5L, TokenValidationResponseTestBuilder.aTokenValidationResponse()
                        .withRole(Role.SUBSCRIBER.name()).withEmail("volcanofan@gmail.com").build()),
                Arguments.of(5L, TokenValidationResponseTestBuilder.aTokenValidationResponse().build()));
    }

    private static Stream<Arguments> getArgumentsForDeleteTest() {
        return Stream.of(
                Arguments.of(5L, TokenValidationResponseTestBuilder.aTokenValidationResponse()
                        .withRole(Role.SUBSCRIBER.name()).withEmail("volcanofan@gmail.com").build()),
                Arguments.of(9L, TokenValidationResponseTestBuilder.aTokenValidationResponse().build()));
    }

}
