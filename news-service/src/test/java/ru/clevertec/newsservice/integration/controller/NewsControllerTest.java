package ru.clevertec.newsservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
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
import ru.clevertec.newsservice.dto.news.NewsRequest;
import ru.clevertec.newsservice.dto.user.Role;
import ru.clevertec.newsservice.dto.user.TokenValidationResponse;
import ru.clevertec.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.newsservice.util.json.CommonErrorJsonSupplier;
import ru.clevertec.newsservice.util.json.NewsJsonSupplier;
import ru.clevertec.newsservice.util.testbuilder.news.NewsRequestTestBuilder;
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
class NewsControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            long id = 5;
            String json = NewsJsonSupplier.getNewsResponse();

            mockMvc.perform(get("/news/" + id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 122;
            String json = NewsJsonSupplier.getNotFoundGetNewsResponse();

            mockMvc.perform(get("/news/" + wrongId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if id is not positive")
        void testShouldReturnExpectedJsonAndStatus409IfIsNotPositive() throws Exception {
            long wrongId = -1L;
            String json = CommonErrorJsonSupplier.getPositiveErrorGetResponse();

            mockMvc.perform(get("/news/" + wrongId))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class FindAllGetEndpointTest {

        @Test
        @DisplayName("test should return empty json and status 200 if there is no news on the page")
        void testShouldReturnEmptyJsonAndStatus200IfThereIsNoNewsOnThePage() throws Exception {
            int page = 3;
            String json = "[]";

            mockMvc.perform(get("/news?page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            int page = 0;
            int size = 5;
            String json = NewsJsonSupplier.getAllNewsResponses();

            mockMvc.perform(get("/news?page=" + page + "&size=" + size))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 406 if typo in sort")
        void testShouldReturnExpectedJsonAndStatus406IfTypoInSort() throws Exception {
            int page = 0;
            int size = 5;
            String typoInSort = "tit";
            String json = NewsJsonSupplier.getTypoInSortNewsResponse();

            mockMvc.perform(get("/news?page=" + page + "&size=" + size + "&sort=" + typoInSort))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class FindAllByMatchingTextParamsGetEndpointTest {

        @Test
        @DisplayName("test should return empty json and status 200 if there is no news on the page")
        void testShouldReturnEmptyJsonAndStatus200IfThereIsNoNewsOnThePage() throws Exception {
            int page = 3;
            String text = "в России";
            String title = "в россии";
            String json = "[]";

            mockMvc.perform(get("/news/params?text=" + text + "&title=" + title + "&page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            int page = 0;
            String text = "в России";
            String title = "в россии";
            String json = NewsJsonSupplier.getMatcherNewsResponse();

            mockMvc.perform(get("/news/params?text=" + text + "&title=" + title + "&page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 406 if typo in sort")
        void testShouldReturnExpectedJsonAndStatus406IfTypoInSort() throws Exception {
            String text = "в России";
            String title = "в россии";
            String typoInSort = "tit";
            String json = NewsJsonSupplier.getTypoInSortNewsResponse();

            mockMvc.perform(get("/news/params?text=" + text + "&title=" + title + "&sort=" + typoInSort))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    @WireMockTest(httpPort = 7070)
    class SavePostEndpointTest {

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test should return expected json and status 201 for Journalist and Admin")
        @MethodSource("ru.clevertec.newsservice.integration.controller.NewsControllerTest#getArgumentsForPostTest")
        void testShouldReturnExpectedJsonAndStatus201ForJournalistAndAdmin(Long expectedId,
                                                                           TokenValidationResponse response) throws Exception {
            NewsRequest newsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(newsRequest);
            String json = objectMapper.writeValueAsString(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(post("/news")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(expectedId))
                    .andExpect(jsonPath("$.title").value(newsRequest.title()))
                    .andExpect(jsonPath("$.text").value(newsRequest.text()))
                    .andExpect(jsonPath("$.email").value(response.email()));
        }

        @Test
        @DisplayName("test should return expected json and status 401 if user is unauthorized")
        void testShouldReturnExpectedJsonAndStatus401IfUserIsUnauthorized() throws Exception {
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(request);
            String json = CommonErrorJsonSupplier.getUnauthorizedErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(401)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(post("/news")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 403 for Subscriber")
        void testShouldReturnExpectedJsonAndStatus403ForSubscriber() throws Exception {
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(request);
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name()).build();
            String json = objectMapper.writeValueAsString(response);
            String expectedJson = CommonErrorJsonSupplier.getForbiddenForSubscriberErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(post("/news")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if text is blank")
        void testShouldReturnExpectedJsonAndStatus409IfTitleIsBlank() throws Exception {
            NewsRequest newsRequest = NewsRequestTestBuilder.aNewsRequest().withText("").build();
            String content = objectMapper.writeValueAsString(newsRequest);
            String json = CommonErrorJsonSupplier.getNotBlankErrorResponse();

            mockMvc.perform(post("/news")
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
        @DisplayName("test should return expected json and status 201 for Journalist and Admin")
        @MethodSource("ru.clevertec.newsservice.integration.controller.NewsControllerTest#getArgumentsForPutTest")
        void testShouldReturnExpectedJsonAndStatus201ForJournalistAndAdmin(Long expectedId,
                                                                           TokenValidationResponse response) throws Exception {
            NewsRequest newsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(newsRequest);
            String json = objectMapper.writeValueAsString(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(put("/news/" + expectedId)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(expectedId))
                    .andExpect(jsonPath("$.title").value(newsRequest.title()))
                    .andExpect(jsonPath("$.text").value(newsRequest.text()))
                    .andExpect(jsonPath("$.email").value(response.email()));
        }

        @Test
        @DisplayName("test should return expected json and status 401 if user is unauthorized")
        void testShouldReturnExpectedJsonAndStatus401IfUserIsUnauthorized() throws Exception {
            long id = 2L;
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(request);
            String json = CommonErrorJsonSupplier.getUnauthorizedErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(401)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(put("/news/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 403 for Subscriber")
        void testShouldReturnExpectedJsonAndStatus403ForSubscriber() throws Exception {
            long id = 2L;
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(request);
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name()).build();
            String json = objectMapper.writeValueAsString(response);
            String expectedJson = CommonErrorJsonSupplier.getForbiddenForSubscriberErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(put("/news/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 122;
            NewsRequest newsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(newsRequest);
            String expectedJson = NewsJsonSupplier.getNotFoundPutNewsResponse();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(put("/news/" + wrongId)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 405 for Journalist with wrong permission")
        void testShouldReturnExpectedJsonAndStatus405ForJournalistWithWrongPermission() throws Exception {
            long id = 2L;
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(request);
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.JOURNALIST.name()).build();
            String json = objectMapper.writeValueAsString(response);
            String expectedJson = CommonErrorJsonSupplier.getMethodNotAllowedForJournalistErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(put("/news/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isMethodNotAllowed())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if text is blank")
        void testShouldReturnExpectedJsonAndStatus409IfTitleIsBlank() throws Exception {
            long id = 5;
            NewsRequest newsRequest = NewsRequestTestBuilder.aNewsRequest().withText("").build();
            String content = objectMapper.writeValueAsString(newsRequest);
            String json = CommonErrorJsonSupplier.getNotBlankErrorResponse();

            mockMvc.perform(put("/news/" + id)
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
        @DisplayName("test should return expected json and status 200 for Journalist and Admin")
        @MethodSource("ru.clevertec.newsservice.integration.controller.NewsControllerTest#getArgumentsForDeleteTest")
        void testShouldReturnExpectedJsonAndStatus200ForJournalistAndAdmin(Long id,
                                                                           TokenValidationResponse response) throws Exception {
            DeleteResponse deleteResponse = new DeleteResponse("News with ID " + id + " was successfully deleted");
            String expectedJson = objectMapper.writeValueAsString(deleteResponse);
            String json = objectMapper.writeValueAsString(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(delete("/news/" + id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 401 if user is unauthorized")
        void testShouldReturnExpectedJsonAndStatus401IfUserIsUnauthorized() throws Exception {
            long id = 3L;
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(request);
            String json = CommonErrorJsonSupplier.getUnauthorizedErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(401)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(delete("/news/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 403 for Subscriber")
        void testShouldReturnExpectedJsonAndStatus403ForSubscriber() throws Exception {
            long id = 2L;
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(request);
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name()).build();
            String json = objectMapper.writeValueAsString(response);
            String expectedJson = CommonErrorJsonSupplier.getForbiddenForSubscriberErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(delete("/news/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 122;
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String expectedJson = NewsJsonSupplier.getNotFoundDeleteNewsResponse();
            String json = objectMapper.writeValueAsString(response);

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(delete("/news/" + wrongId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 405 for Journalist with wrong permission")
        void testShouldReturnExpectedJsonAndStatus405ForJournalistWithWrongPermission() throws Exception {
            long id = 2L;
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(request);
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.JOURNALIST.name()).build();
            String json = objectMapper.writeValueAsString(response);
            String expectedJson = CommonErrorJsonSupplier.getMethodNotAllowedForJournalistErrorResponse();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            mockMvc.perform(delete("/news/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isMethodNotAllowed())
                    .andExpect(content().json(expectedJson));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if id is not positive")
        void testShouldReturnExpectedJsonAndStatus409IfIsNotPositive() throws Exception {
            long wrongId = -1L;
            String json = CommonErrorJsonSupplier.getPositiveErrorDeleteResponse();

            mockMvc.perform(delete("/news/" + wrongId))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    private static Stream<Arguments> getArgumentsForPostTest() {
        return Stream.of(
                Arguments.of(6L, TokenValidationResponseTestBuilder.aTokenValidationResponse().build()),
                Arguments.of(7L, TokenValidationResponseTestBuilder.aTokenValidationResponse()
                        .withRole(Role.JOURNALIST.name()).build()));
    }

    private static Stream<Arguments> getArgumentsForPutTest() {
        return Stream.of(
                Arguments.of(4L, TokenValidationResponseTestBuilder.aTokenValidationResponse()
                        .withRole(Role.JOURNALIST.name()).withEmail("bjohnson04@outlook.com").build()),
                Arguments.of(4L, TokenValidationResponseTestBuilder.aTokenValidationResponse().build()));
    }

    private static Stream<Arguments> getArgumentsForDeleteTest() {
        return Stream.of(
                Arguments.of(4L, TokenValidationResponseTestBuilder.aTokenValidationResponse()
                        .withRole(Role.JOURNALIST.name()).withEmail("bjohnson04@outlook.com").build()),
                Arguments.of(2L, TokenValidationResponseTestBuilder.aTokenValidationResponse().build()));
    }

}
