package ru.clevertec.newsservice.integration.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.clevertec.exceptionhandlerstarter.exception.AccessDeniedForThisRoleException;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchNewsException;
import ru.clevertec.exceptionhandlerstarter.exception.UserDoesNotHavePermissionException;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.newsservice.dto.proto.NewsRequest;
import ru.clevertec.newsservice.dto.user.Role;
import ru.clevertec.newsservice.dto.user.TokenValidationResponse;
import ru.clevertec.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.newsservice.service.NewsService;
import ru.clevertec.newsservice.util.testbuilder.news.NewsRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.news.NewsResponseTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.user.TokenValidationResponseTestBuilder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@WireMockTest(httpPort = 7070)
class NewsServiceImplTest extends BaseIntegrationTest {

    private final NewsService newsService;
    private final ObjectMapper objectMapper;

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test should throw NoSuchNewsException")
        void testShouldThrowNoSuchNewsException() {
            long wrongId = 17L;

            assertThrows(NoSuchNewsException.class, () -> newsService.findById(wrongId));
        }

        @Test
        @DisplayName("test should throw NoSuchNewsException with expected message")
        void testShouldThrowNoSuchNewsExceptionWithExpectedMessage() {
            long wrongId = 17L;
            String expectedMessage = "News with ID " + wrongId + " does not exist";

            Exception exception = assertThrows(NoSuchNewsException.class, () -> newsService.findById(wrongId));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected NewsResponse")
        void testShouldReturnExpectedNewsResponse() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse()
                    .withId(3L)
                    .build();

            NewsResponse actualValue = newsService.findById(expectedValue.id());

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test should return List of size 5")
        void testShouldReturnListOfSizeOne() {
            int expectedSize = 5;
            Pageable pageable = PageRequest.of(0, 5);

            List<NewsResponse> actualValues = newsService.findAll(pageable);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return List that contains expected value")
        void testShouldReturnListThatContainsExpectedValue() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse()
                    .withTime(LocalDateTime.of(2023, Month.JUNE, 14, 10, 30))
                    .withTitle("Breaking: Volcano erupts in Hawaii")
                    .withText("A massive volcanic eruption has occurred on the Big Island of Hawaii," +
                              " spewing lava and ash into the air. The eruption was triggered by a series of earthquakes" +
                              " that rocked the island in the past few days. Authorities have issued evacuation orders" +
                              " for nearby residents and warned of possible tsunamis and landslides.")
                    .withEmail("reporter@news.com")
                    .build();
            Pageable pageable = PageRequest.of(0, 1);

            List<NewsResponse> actualValues = newsService.findAll(pageable);

            assertThat(actualValues).contains(expectedValue);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            Pageable pageable = PageRequest.of(5, 10);

            List<NewsResponse> actualValues = newsService.findAll(pageable);

            assertThat(actualValues).isEmpty();
        }

        @Test
        @DisplayName("test should return sorted List by id DESC")
        void testShouldReturnSortedListByIdDesc() {
            long expectedId = 1L;
            Sort sort = Sort.by("id").descending();
            Pageable pageable = PageRequest.of(4, 1, sort);

            List<NewsResponse> actualValues = newsService.findAll(pageable);

            assertThat(actualValues.get(0).id()).isEqualTo(expectedId);
        }

        @Test
        @DisplayName("test should return sorted List by email")
        void testShouldReturnSortedListByEmail() {
            String expectedEmail = "health@news.com";
            Sort sort = Sort.by("email");
            Pageable pageable = PageRequest.of(0, 1, sort);

            List<NewsResponse> actualValues = newsService.findAll(pageable);

            assertThat(actualValues.get(0).email()).isEqualTo(expectedEmail);
        }

    }

    @Nested
    class FindAllByMatchingTextParams {

        @Test
        @DisplayName("test should return List of size 1")
        void testShouldReturnListOfSizeOne() {
            Pageable pageable = PageRequest.of(0, 5);
            String title = "SpaceX";
            String text = "SpaceX has successfully";
            int expectedSize = 1;

            List<NewsResponse> actualValues = newsService.findAllByMatchingTextParams(title, text, pageable);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return List that contains expected value matches by title")
        void testShouldReturnListThatContainsExpectedValueMatchesByTitle() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse()
                    .withId(4L)
                    .withTime(LocalDateTime.of(2023, Month.JUNE, 14, 10, 45))
                    .withTitle("World Cup kicks off in Qatar")
                    .withText("The 2023 FIFA World Cup has officially begun in Qatar, with the host nation facing Brazil" +
                              " in the opening match. The World Cup, which is held every four years, is the most" +
                              " prestigious and popular soccer tournament in the world, attracting millions of fans" +
                              " and viewers from around the globe. The World Cup will last for a month, with 32 teams" +
                              " competing for the coveted trophy.")
                    .withEmail("sports@news.com")
                    .build();
            Pageable pageable = PageRequest.of(0, 2);
            String title = "World Cup";
            String text = "";

            List<NewsResponse> actualValues = newsService.findAllByMatchingTextParams(title, text, pageable);

            assertThat(actualValues).contains(expectedValue);
        }

        @Test
        @DisplayName("test should return List that contains expected value matches by text")
        void testShouldReturnListThatContainsExpectedValueMatchesByText() {
            String expectedText = "A new study published in the journal Nature has revealed the secrets of longevity," +
                                  " or how to live longer and healthier lives. The study, which involved analyzing " +
                                  "the genomes of over 10,000 centenarians, or people who live past 100 years old," +
                                  " found that they share certain genetic variants that protect them from age-related" +
                                  " diseases such as cancer, diabetes, and Alzheimer's. The study also identified some" +
                                  " lifestyle factors that contribute to longevity, such as eating a balanced diet," +
                                  " exercising regularly, and maintaining social connections.";
            Pageable pageable = PageRequest.of(0, 2);
            String title = "";
            String text = "A new study";

            List<NewsResponse> actualValues = newsService.findAllByMatchingTextParams(title, text, pageable);

            assertThat(actualValues.get(0).text()).isEqualTo(expectedText);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            Pageable pageable = PageRequest.of(0, 5);
            String title = "title";
            String text = "text";

            List<NewsResponse> actualValues = newsService.findAllByMatchingTextParams(title, text, pageable);

            assertThat(actualValues).isEmpty();
        }

        @Test
        @DisplayName("test should return sorted List by title")
        void testShouldReturnSortedListByTitle() {
            String expectedTitle = "Apple unveils iPhone 15 with holographic display";
            Sort sort = Sort.by("title");
            Pageable pageable = PageRequest.of(0, 1, sort);
            String title = "";
            String text = "a";

            List<NewsResponse> actualValues = newsService.findAllByMatchingTextParams(title, text, pageable);

            assertThat(actualValues.get(0).title()).isEqualTo(expectedTitle);
        }

    }

    @Nested
    @WireMockTest(httpPort = 7070)
    class SaveTest {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() throws JsonProcessingException {
            String token = "jwt";
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse()
                    .withTime(LocalDateTime.now())
                    .withTitle(request.getTitle())
                    .withText(request.getText())
                    .withEmail(response.email())
                    .build();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            NewsResponse actualValue = newsService.save(request, token);

            assertAll(
                    () -> assertThat(actualValue.title()).isEqualTo(expectedValue.title()),
                    () -> assertThat(actualValue.text()).isEqualTo(expectedValue.text()),
                    () -> assertThat(actualValue.email()).isEqualTo(expectedValue.email()),
                    () -> assertThat(actualValue.time().getMonthValue()).isEqualTo(expectedValue.time().getMonthValue()),
                    () -> assertThat(actualValue.time().getHour()).isEqualTo(expectedValue.time().getHour()),
                    () -> assertThat(actualValue.time().getMinute()).isEqualTo(expectedValue.time().getMinute())
            );
        }

        @Test
        @DisplayName("test should throw AccessDeniedForThisRoleException with expected message")
        void testShouldThrowAccessDeniedForThisRoleExceptionWithExpectedMessage() throws JsonProcessingException {
            String token = "jwt";
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name())
                    .build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "Access Denied for role: " + response.role();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(AccessDeniedForThisRoleException.class, () -> newsService.save(request, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    @WireMockTest(httpPort = 7070)
    class UpdateByIdTest {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() throws JsonProcessingException {
            long id = 1L;
            String token = "jwt";
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse()
                    .withTime(LocalDateTime.now())
                    .withTitle(request.getTitle())
                    .withText(request.getText())
                    .withEmail(response.email())
                    .build();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            NewsResponse actualValue = newsService.updateById(id, request, token);

            assertAll(
                    () -> assertThat(actualValue.title()).isEqualTo(expectedValue.title()),
                    () -> assertThat(actualValue.text()).isEqualTo(expectedValue.text()),
                    () -> assertThat(actualValue.email()).isEqualTo(expectedValue.email()),
                    () -> assertThat(actualValue.time().getMonthValue()).isEqualTo(expectedValue.time().getMonthValue()),
                    () -> assertThat(actualValue.time().getHour()).isEqualTo(expectedValue.time().getHour()),
                    () -> assertThat(actualValue.time().getMinute()).isEqualTo(expectedValue.time().getMinute())
            );
        }

        @Test
        @DisplayName("test should throw AccessDeniedForThisRoleException with expected message")
        void testShouldThrowAccessDeniedForThisRoleExceptionWithExpectedMessage() throws JsonProcessingException {
            long id = 1L;
            String token = "jwt";
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name())
                    .build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "Access Denied for role: " + response.role();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(AccessDeniedForThisRoleException.class,
                    () -> newsService.updateById(id, request, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw NoSuchNewsException with expected message")
        void testShouldThrowNoSuchNewsExceptionWithExpectedMessage() throws JsonProcessingException {
            long id = 0;
            String token = "jwt";
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "There is no News with ID " + id + " to update";

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(NoSuchNewsException.class, () -> newsService.updateById(id, request, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw UserDoesNotHavePermissionException with expected message")
        void testShouldThrowUserDoesNotHavePermissionExceptionWithExpectedMessage() throws JsonProcessingException {
            long id = 3L;
            String token = "jwt";
            NewsRequest request = NewsRequestTestBuilder.aNewsRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.JOURNALIST.name())
                    .build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "With role " + response.role() + " you can update or delete only your own news/comments";

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(UserDoesNotHavePermissionException.class,
                    () -> newsService.updateById(id, request, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    @WireMockTest(httpPort = 7070)
    class DeleteByIdTest {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() throws JsonProcessingException {
            long id = 1L;
            String token = "jwt";
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);
            DeleteResponse expectedValue = new DeleteResponse("News with ID " + id + " was successfully deleted");

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            DeleteResponse actualValue = newsService.deleteById(id, token);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw AccessDeniedForThisRoleException with expected message")
        void testShouldThrowAccessDeniedForThisRoleExceptionWithExpectedMessage() throws JsonProcessingException {
            long id = 1L;
            String token = "jwt";
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name())
                    .build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "Access Denied for role: " + response.role();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(AccessDeniedForThisRoleException.class,
                    () -> newsService.deleteById(id, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw NoSuchNewsException with expected message")
        void testShouldThrowNoSuchNewsExceptionWithExpectedMessage() throws JsonProcessingException {
            long id = 0;
            String token = "jwt";
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "There is no News with ID " + id + " to delete";

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(NoSuchNewsException.class, () -> newsService.deleteById(id, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw UserDoesNotHavePermissionException with expected message")
        void testShouldThrowUserDoesNotHavePermissionExceptionWithExpectedMessage() throws JsonProcessingException {
            long id = 3L;
            String token = "jwt";
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.JOURNALIST.name())
                    .build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "With role " + response.role() + " you can update or delete only your own news/comments";

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(UserDoesNotHavePermissionException.class,
                    () -> newsService.deleteById(id, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

}
