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
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchCommentException;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchNewsException;
import ru.clevertec.exceptionhandlerstarter.exception.UserDoesNotHavePermissionException;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.comment.CommentRequest;
import ru.clevertec.newsservice.dto.comment.CommentResponse;
import ru.clevertec.newsservice.dto.comment.CommentWithNewsRequest;
import ru.clevertec.newsservice.dto.news.NewsWithCommentsResponse;
import ru.clevertec.newsservice.dto.user.Role;
import ru.clevertec.newsservice.dto.user.TokenValidationResponse;
import ru.clevertec.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.newsservice.service.CommentService;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentResponseTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentWithNewsRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.news.NewsWithCommentsResponseTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.user.TokenValidationResponseTestBuilder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
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
class CommentServiceImplTest extends BaseIntegrationTest {

    private final CommentService commentService;
    private final ObjectMapper objectMapper;

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test should throw NoSuchCommentException")
        void testShouldThrowNoSuchCommentException() {
            long wrongId = 117L;

            assertThrows(NoSuchCommentException.class, () -> commentService.findById(wrongId));
        }

        @Test
        @DisplayName("test should throw NoSuchCommentException with expected message")
        void testShouldThrowNoSuchCommentExceptionWithExpectedMessage() {
            long wrongId = 117L;
            String expectedMessage = "Comment with ID " + wrongId + " does not exist";

            Exception exception = assertThrows(NoSuchCommentException.class, () -> commentService.findById(wrongId));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected CommentResponse")
        void testShouldReturnExpectedCommentResponse() {
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse()
                    .withTime(LocalDateTime.of(2023, Month.JUNE, 1, 12, 34, 56))
                    .withText("Отличная новость, спасибо за информацию!")
                    .withUsername("Иван")
                    .withEmail("jsmith01@gmail.com")
                    .build();

            CommentResponse actualValue = commentService.findById(expectedValue.id());

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindNewsByNewsIdWithComments {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() {
            long newsId = 3L;
            NewsWithCommentsResponse expectedValue = NewsWithCommentsResponseTestBuilder.aNewsWithCommentsResponse()
                    .withId(newsId)
                    .withTime(LocalDateTime.of(2023, Month.MAY, 21, 19, 30, 59))
                    .withTitle("В Иране продолжаются протесты против правительства")
                    .withText("В Иране продолжаются массовые протесты против правительства, которое обвиняют в коррупции и нарушении прав человека.")
                    .withEmail("klee03@hotmail.com")
                    .withComments(Collections.singletonList(CommentResponseTestBuilder.aCommentResponse()
                            .withId(11L)
                            .withTime(LocalDateTime.of(2023, Month.JUNE, 11, 9, 34, 12))
                            .withText("Не очень понятно, надо бы больше примеров")
                            .withUsername("Максим")
                            .withEmail("jjackson11@hotmail.com")
                            .build()))
                    .build();
            Pageable pageable = PageRequest.of(0, 1);

            NewsWithCommentsResponse actualValue = commentService.findNewsByNewsIdWithComments(newsId, pageable);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should return NewsWithCommentsResponse with List of Comments size 2")
        void testShouldReturnNewsWithCommentsResponseWithListOfSizeTwo() {
            long newsId = 3L;
            Pageable pageable = PageRequest.of(0, 2);
            int expectedSize = 2;

            NewsWithCommentsResponse actualValue = commentService.findNewsByNewsIdWithComments(newsId, pageable);

            assertThat(actualValue.comments()).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return NewsWithCommentsResponse with empty List of Comments")
        void testShouldReturnNewsWithCommentsResponseWithEmptyListOfComments() {
            long newsId = 3L;
            Pageable pageable = PageRequest.of(5, 10);

            NewsWithCommentsResponse actualValue = commentService.findNewsByNewsIdWithComments(newsId, pageable);

            assertThat(actualValue.comments()).isEmpty();
        }

        @Test
        @DisplayName("test should throw NoSuchNewsException with expected message")
        void testShouldThrowNoSuchNewsExceptionWithExpectedMessage() {
            long wrongId = 33L;
            String expectedMessage = "News with ID " + wrongId + " does not exist";
            Pageable pageable = PageRequest.of(5, 10);

            Exception exception = assertThrows(NoSuchNewsException.class,
                    () -> commentService.findNewsByNewsIdWithComments(wrongId, pageable));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return NewsWithCommentsResponse with sorted List of comments by id DESC")
        void testShouldReturnNewsWithCommentsResponseWithSortedListOfCommentsByIdDesc() {
            long expectedId = 11L;
            long newsId = 3L;
            Sort sort = Sort.by("id").descending();
            Pageable pageable = PageRequest.of(4, 1, sort);

            NewsWithCommentsResponse actualValue = commentService.findNewsByNewsIdWithComments(newsId, pageable);

            assertThat(actualValue.comments().get(0).id()).isEqualTo(expectedId);
        }

        @Test
        @DisplayName("test should return NewsWithCommentsResponse with sorted List of comments by email")
        void testShouldReturnSortedListByEmail() {
            String expectedEmail = "jjackson11@hotmail.com";
            long newsId = 3L;
            Sort sort = Sort.by("email");
            Pageable pageable = PageRequest.of(0, 1, sort);

            NewsWithCommentsResponse actualValue = commentService.findNewsByNewsIdWithComments(newsId, pageable);

            assertThat(actualValue.comments().get(0).email()).isEqualTo(expectedEmail);
        }

    }

    @Nested
    class FindAllByMatchingTextParams {

        @Test
        @DisplayName("test should return List of size 1")
        void testShouldReturnListOfSizeOne() {
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest()
                    .withUsername("Анна")
                    .withText("")
                    .build();
            Pageable pageable = PageRequest.of(0, 5);
            int expectedSize = 1;

            List<CommentResponse> actualValues = commentService.findAllByMatchingTextParams(request, pageable);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return List that contains expected value matches by username")
        void testShouldReturnListThatContainsExpectedValueMatchesByUsername() {
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse()
                    .withId(3L)
                    .withTime(LocalDateTime.of(2023, Month.JUNE, 2, 9, 12, 8))
                    .withText("Какая интересная статья, мне очень понравилось")
                    .withUsername("Анна")
                    .withEmail("klee03@hotmail.com")
                    .build();
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest()
                    .withUsername("Анна")
                    .withText("")
                    .build();
            Pageable pageable = PageRequest.of(0, 2);

            List<CommentResponse> actualValues = commentService.findAllByMatchingTextParams(request, pageable);

            assertThat(actualValues).contains(expectedValue);
        }

        @Test
        @DisplayName("test should return List that contains expected value matches by text")
        void testShouldReturnListThatContainsExpectedValueMatchesByText() {
            String expectedText = "Все это бессмысленно, никто не будет использовать это";
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest()
                    .withUsername("")
                    .withText("Все")
                    .build();
            Pageable pageable = PageRequest.of(0, 2);

            List<CommentResponse> actualValues = commentService.findAllByMatchingTextParams(request, pageable);

            assertThat(actualValues.get(0).text()).isEqualTo(expectedText);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest()
                    .withUsername("Noname")
                    .withText("Меня не существует")
                    .build();
            Pageable pageable = PageRequest.of(0, 5);

            List<CommentResponse> actualValues = commentService.findAllByMatchingTextParams(request, pageable);

            assertThat(actualValues).isEmpty();
        }

        @Test
        @DisplayName("test should return sorted List by username")
        void testShouldReturnSortedListByUsername() {
            String expectedUsername = "Анна";
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest()
                    .withUsername("Анна")
                    .withText("")
                    .build();
            Sort sort = Sort.by("username");
            Pageable pageable = PageRequest.of(0, 1, sort);

            List<CommentResponse> actualValues = commentService.findAllByMatchingTextParams(request, pageable);

            assertThat(actualValues.get(0).username()).isEqualTo(expectedUsername);
        }

    }

    @Nested
    @WireMockTest(httpPort = 7070)
    class SaveTest {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() throws JsonProcessingException {
            String token = "jwt";
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse()
                    .withTime(LocalDateTime.now())
                    .withUsername(request.username())
                    .withText(request.text())
                    .withEmail(response.email())
                    .build();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            CommentResponse actualValue = commentService.save(request, token);

            assertAll(
                    () -> assertThat(actualValue.username()).isEqualTo(expectedValue.username()),
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
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.JOURNALIST.name())
                    .build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "Access Denied for role: " + response.role();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(AccessDeniedForThisRoleException.class,
                    () -> commentService.save(request, token));
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
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse()
                    .withTime(LocalDateTime.now())
                    .withUsername(request.username())
                    .withText(request.text())
                    .withEmail(response.email())
                    .build();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            CommentResponse actualValue = commentService.updateById(id, request, token);

            assertAll(
                    () -> assertThat(actualValue.username()).isEqualTo(expectedValue.username()),
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
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.JOURNALIST.name())
                    .build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "Access Denied for role: " + response.role();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(AccessDeniedForThisRoleException.class,
                    () -> commentService.updateById(id, request, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw NoSuchCommentException with expected message")
        void testShouldThrowNoSuchCommentExceptionWithExpectedMessage() throws JsonProcessingException {
            long id = 0;
            String token = "jwt";
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "There is no Comment with ID " + id + " to update";

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(NoSuchCommentException.class,
                    () -> commentService.updateById(id, request, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw UserDoesNotHavePermissionException with expected message")
        void testShouldThrowUserDoesNotHavePermissionExceptionWithExpectedMessage() throws JsonProcessingException {
            long id = 3L;
            String token = "jwt";
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name())
                    .build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "With role " + response.role() + " you can update or delete only your own news/comments";

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(UserDoesNotHavePermissionException.class,
                    () -> commentService.updateById(id, request, token));
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
            DeleteResponse expectedValue = new DeleteResponse("Comment with ID " + id + " was successfully deleted");

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            DeleteResponse actualValue = commentService.deleteById(id, token);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw AccessDeniedForThisRoleException with expected message")
        void testShouldThrowAccessDeniedForThisRoleExceptionWithExpectedMessage() throws JsonProcessingException {
            long id = 1L;
            String token = "jwt";
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.JOURNALIST.name())
                    .build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "Access Denied for role: " + response.role();

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(AccessDeniedForThisRoleException.class,
                    () -> commentService.deleteById(id, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw NoSuchCommentException with expected message")
        void testShouldThrowNoSuchCommentExceptionWithExpectedMessage() throws JsonProcessingException {
            long id = 0;
            String token = "jwt";
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "There is no Comment with ID " + id + " to delete";

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(NoSuchCommentException.class, () -> commentService.deleteById(id, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw UserDoesNotHavePermissionException with expected message")
        void testShouldThrowUserDoesNotHavePermissionExceptionWithExpectedMessage() throws JsonProcessingException {
            long id = 3L;
            String token = "jwt";
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse()
                    .withRole(Role.SUBSCRIBER.name())
                    .build();
            String json = objectMapper.writeValueAsString(response);
            String expectedMessage = "With role " + response.role() + " you can update or delete only your own news/comments";

            stubFor(WireMock.post(urlEqualTo("/users/validate"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withBody(json)
                            .withHeader(CONTENT_TYPE, APPLICATION_JSON.toString())));

            Exception exception = assertThrows(UserDoesNotHavePermissionException.class,
                    () -> commentService.deleteById(id, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

}
