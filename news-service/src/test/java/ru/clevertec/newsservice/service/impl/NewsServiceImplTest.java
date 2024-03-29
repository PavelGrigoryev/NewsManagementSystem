package ru.clevertec.newsservice.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchNewsException;
import ru.clevertec.newsservice.dto.proto.DeleteResponse;
import ru.clevertec.newsservice.dto.proto.NewsRequest;
import ru.clevertec.newsservice.dto.proto.NewsResponse;
import ru.clevertec.newsservice.dto.proto.NewsResponseList;
import ru.clevertec.newsservice.dto.proto.Role;
import ru.clevertec.newsservice.dto.proto.TokenValidationResponse;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.model.News;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.service.AuthenticationService;
import ru.clevertec.newsservice.util.testbuilder.ExampleMatcherTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.news.NewsRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.news.NewsResponseTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.news.NewsTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.user.TokenValidationResponseTestBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl newsService;
    @Mock
    private NewsRepository newsRepository;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private NewsMapper newsMapper;
    @Captor
    private ArgumentCaptor<News> captor;

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test should throw NoSuchNewsException")
        void testShouldThrowNoSuchNewsException() {
            long id = 2L;

            doThrow(new NoSuchNewsException(""))
                    .when(newsRepository)
                    .findById(id);

            assertThrows(NoSuchNewsException.class, () -> newsService.findById(id));
        }

        @Test
        @DisplayName("test should throw NoSuchNewsException with expected message")
        void testShouldThrowNoSuchNewsExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "News with ID " + id + " does not exist";

            Exception exception = assertThrows(NoSuchNewsException.class, () -> newsService.findById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected NewsResponse")
        void testShouldReturnExpectedNewsResponse() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse().build();
            News mockedNews = NewsTestBuilder.aNews().build();
            long id = expectedValue.getId();

            doReturn(expectedValue)
                    .when(newsMapper)
                    .toResponse(mockedNews);

            doReturn(Optional.of(mockedNews))
                    .when(newsRepository)
                    .findById(id);

            NewsResponse actualValue = newsService.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test should return List of size 1")
        void testShouldReturnListOfSizeOne() {
            NewsResponse mockedNewsResponse = NewsResponseTestBuilder.aNewsResponse().build();
            News mockedNews = NewsTestBuilder.aNews().build();
            Page<News> mockedPage = new PageImpl<>(List.of(mockedNews));
            Pageable mockedPageable = PageRequest.of(1, 2);
            int expectedSize = 1;

            doReturn(List.of(mockedNewsResponse))
                    .when(newsMapper)
                    .toResponses(mockedPage);

            doReturn(mockedPage)
                    .when(newsRepository)
                    .findAll(mockedPageable);

            NewsResponseList actualValues = newsService.findAll(mockedPageable);

            assertThat(actualValues.getNewsList()).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return List that contains expected value")
        void testShouldReturnListThatContainsExpectedValue() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse().build();
            News mockedNews = NewsTestBuilder.aNews().build();
            Page<News> mockedPage = new PageImpl<>(List.of(mockedNews));
            Pageable mockedPageable = PageRequest.of(1, 2);

            doReturn(List.of(expectedValue))
                    .when(newsMapper)
                    .toResponses(mockedPage);

            doReturn(mockedPage)
                    .when(newsRepository)
                    .findAll(mockedPageable);

            NewsResponseList actualValues = newsService.findAll(mockedPageable);

            assertThat(actualValues.getNewsList()).contains(expectedValue);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            Pageable mockedPageable = PageRequest.of(1, 2);

            doReturn(Page.empty())
                    .when(newsRepository)
                    .findAll(mockedPageable);

            NewsResponseList actualValues = newsService.findAll(mockedPageable);

            assertThat(actualValues.getNewsList()).isEmpty();
        }

    }

    @Nested
    class FindAllByMatchingTextParams {

        @Test
        @DisplayName("test should return List of size 1")
        void testShouldReturnListOfSizeOne() {
            NewsResponse mockedNewsResponse = NewsResponseTestBuilder.aNewsResponse().build();
            News mockedNews = NewsTestBuilder.aNews().build();
            ExampleMatcher exampleMatcher = ExampleMatcherTestBuilder.aExampleMatcher().build();
            Example<News> mockedNewsExample = Example.of(mockedNews, exampleMatcher);
            Page<News> mockedPage = new PageImpl<>(List.of(mockedNews));
            Pageable mockedPageable = PageRequest.of(1, 2);
            String title = "title";
            String text = "text";
            int expectedSize = 1;

            doReturn(mockedNews)
                    .when(newsMapper)
                    .fromParams(title, text);

            doReturn(List.of(mockedNewsResponse))
                    .when(newsMapper)
                    .toResponses(mockedPage);

            doReturn(mockedPage)
                    .when(newsRepository)
                    .findAll(mockedNewsExample, mockedPageable);

            NewsResponseList actualValues = newsService.findAllByMatchingTextParams(title, text, mockedPageable);

            assertThat(actualValues.getNewsList()).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return List that contains expected value")
        void testShouldReturnListThatContainsExpectedValue() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse().build();
            News mockedNews = NewsTestBuilder.aNews().build();
            ExampleMatcher exampleMatcher = ExampleMatcherTestBuilder.aExampleMatcher().build();
            Example<News> mockedNewsExample = Example.of(mockedNews, exampleMatcher);
            Page<News> mockedPage = new PageImpl<>(List.of(mockedNews));
            Pageable mockedPageable = PageRequest.of(1, 2);
            String title = "title";
            String text = "text";

            doReturn(mockedNews)
                    .when(newsMapper)
                    .fromParams(title, text);

            doReturn(List.of(expectedValue))
                    .when(newsMapper)
                    .toResponses(mockedPage);

            doReturn(mockedPage)
                    .when(newsRepository)
                    .findAll(mockedNewsExample, mockedPageable);

            NewsResponseList actualValues = newsService.findAllByMatchingTextParams(title, text, mockedPageable);

            assertThat(actualValues.getNewsList()).contains(expectedValue);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            Pageable mockedPageable = PageRequest.of(1, 2);
            News mockedNews = NewsTestBuilder.aNews().build();
            ExampleMatcher exampleMatcher = ExampleMatcherTestBuilder.aExampleMatcher().build();
            Example<News> mockedNewsExample = Example.of(mockedNews, exampleMatcher);
            String title = "title";
            String text = "text";

            doReturn(mockedNews)
                    .when(newsMapper)
                    .fromParams(title, text);

            doReturn(Page.empty())
                    .when(newsRepository)
                    .findAll(mockedNewsExample, mockedPageable);

            NewsResponseList actualValues = newsService.findAllByMatchingTextParams(title, text, mockedPageable);

            assertThat(actualValues.getNewsList()).isEmpty();
        }

    }

    @Nested
    class SaveTest {

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test should capture saved value")
        @MethodSource("ru.clevertec.newsservice.service.impl.NewsServiceImplTest#getArgumentsForSaveTest")
        void testShouldCaptureValue(News expectedValue) {
            NewsResponse mockedResponse = NewsResponseTestBuilder.aNewsResponse()
                    .withId(expectedValue.getId())
                    .withTitle(expectedValue.getTitle())
                    .withText(expectedValue.getText())
                    .build();
            NewsRequest mockedNewsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.JOURNALIST);

            doReturn(expectedValue)
                    .when(newsMapper)
                    .fromRequest(mockedNewsRequest);

            doReturn(expectedValue)
                    .when(newsRepository)
                    .save(expectedValue);

            doReturn(mockedResponse)
                    .when(newsMapper)
                    .toResponse(expectedValue);

            newsService.save(mockedNewsRequest, token);
            verify(newsRepository).save(captor.capture());

            News captorValue = captor.getValue();
            assertThat(captorValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class UpdateByIdTest {

        @Test
        @DisplayName("test should return updated value")
        void testShouldReturnUpdatedTagDto() {
            News mockedNews = NewsTestBuilder.aNews().build();
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse().build();
            NewsRequest mockedNewsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            long id = mockedNews.getId();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.JOURNALIST);

            doReturn(Optional.of(mockedNews))
                    .when(newsRepository)
                    .findById(id);

            doReturn(mockedNews)
                    .when(newsRepository)
                    .saveAndFlush(mockedNews);

            doReturn(expectedValue)
                    .when(newsMapper)
                    .toResponse(mockedNews);

            NewsResponse actualValue = newsService.updateById(id, mockedNewsRequest, token);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw NoSuchNewsException")
        void testShouldThrowNoSuchNewsException() {
            NewsRequest mockedNewsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            long id = 2L;
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.JOURNALIST);

            doThrow(new NoSuchNewsException(""))
                    .when(newsRepository)
                    .findById(id);

            assertThrows(NoSuchNewsException.class, () -> newsService.updateById(id, mockedNewsRequest, token));
        }

        @Test
        @DisplayName("test should throw NoSuchNewsException with expected message")
        void testShouldThrowNoSuchNewsExceptionWithExpectedMessage() {
            NewsRequest mockedNewsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            long id = 1L;
            String expectedMessage = "There is no News with ID " + id + " to update";
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.JOURNALIST);

            Exception exception = assertThrows(NoSuchNewsException.class,
                    () -> newsService.updateById(id, mockedNewsRequest, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    class DeleteByIdTest {

        @Test
        @DisplayName("test should return expected DeleteResponse")
        void testShouldReturnExpectedDeleteResponse() {
            News mockedNews = NewsTestBuilder.aNews().build();
            long id = mockedNews.getId();
            DeleteResponse expectedValue = DeleteResponse.newBuilder()
                    .setMessage("News with ID " + id + " was successfully deleted")
                    .build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.JOURNALIST);

            doReturn(Optional.of(mockedNews))
                    .when(newsRepository)
                    .findById(id);

            doNothing()
                    .when(newsRepository)
                    .delete(mockedNews);

            DeleteResponse actualValue = newsService.deleteById(id, token);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should invoke method 1 time")
        void testShouldInvokeOneTime() {
            News mockedNews = NewsTestBuilder.aNews().build();
            long id = mockedNews.getId();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.JOURNALIST);

            doReturn(Optional.of(mockedNews))
                    .when(newsRepository)
                    .findById(id);

            doNothing()
                    .when(newsRepository)
                    .delete(mockedNews);

            newsService.deleteById(id, token);

            verify(newsRepository, times(1))
                    .delete(mockedNews);
        }

        @Test
        @DisplayName("test should throw NoSuchNewsException")
        void testShouldThrowNoSuchNewsException() {
            long id = 2L;
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.JOURNALIST);

            doThrow(new NoSuchNewsException(""))
                    .when(newsRepository)
                    .findById(id);

            assertThrows(NoSuchNewsException.class, () -> newsService.deleteById(id, token));
        }

        @Test
        @DisplayName("test should throw NoSuchNewsException with expected message")
        void testShouldThrowNoSuchNewsExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "There is no News with ID " + id + " to delete";
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.JOURNALIST);

            Exception exception = assertThrows(NoSuchNewsException.class, () -> newsService.deleteById(id, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    private static Stream<Arguments> getArgumentsForSaveTest() {
        return Stream.of(
                Arguments.of(NewsTestBuilder.aNews().build()),
                Arguments.of(NewsTestBuilder.aNews().withId(2L)
                        .withTitle("Hot news")
                        .build()),
                Arguments.of(NewsTestBuilder.aNews().withId(3L)
                        .withTitle("Bad news")
                        .withText("Holly")
                        .build()));
    }

}
