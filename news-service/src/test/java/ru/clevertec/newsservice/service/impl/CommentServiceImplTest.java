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
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchCommentException;
import ru.clevertec.newsservice.dto.proto.CommentRequest;
import ru.clevertec.newsservice.dto.proto.CommentResponse;
import ru.clevertec.newsservice.dto.proto.CommentResponseList;
import ru.clevertec.newsservice.dto.proto.CommentWithNewsRequest;
import ru.clevertec.newsservice.dto.proto.DeleteResponse;
import ru.clevertec.newsservice.dto.proto.NewsResponse;
import ru.clevertec.newsservice.dto.proto.NewsWithCommentsResponse;
import ru.clevertec.newsservice.dto.proto.Role;
import ru.clevertec.newsservice.dto.proto.TokenValidationResponse;
import ru.clevertec.newsservice.mapper.CommentMapper;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.model.Comment;
import ru.clevertec.newsservice.model.News;
import ru.clevertec.newsservice.repository.CommentRepository;
import ru.clevertec.newsservice.service.AuthenticationService;
import ru.clevertec.newsservice.service.NewsService;
import ru.clevertec.newsservice.util.testbuilder.ExampleMatcherTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentResponseTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentWithNewsRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.news.NewsResponseTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.news.NewsTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.news.NewsWithCommentsResponseTestBuilder;
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
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private NewsService newsService;
    @Mock
    private NewsMapper newsMapper;
    @Captor
    private ArgumentCaptor<Comment> captor;

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test should throw NoSuchCommentException")
        void testShouldThrowNoSuchCommentException() {
            long id = 2L;

            doThrow(new NoSuchCommentException(""))
                    .when(commentRepository)
                    .findById(id);

            assertThrows(NoSuchCommentException.class, () -> commentService.findById(id));
        }

        @Test
        @DisplayName("test should throw NoSuchCommentException with expected message")
        void testShouldThrowNoSuchCommentExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "Comment with ID " + id + " does not exist";

            Exception exception = assertThrows(NoSuchCommentException.class, () -> commentService.findById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected CommentResponse")
        void testShouldReturnExpectedCommentResponse() {
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse().build();
            Comment mockedComment = CommentTestBuilder.aComment().build();
            long id = expectedValue.getId();

            doReturn(expectedValue)
                    .when(commentMapper)
                    .toResponse(mockedComment);

            doReturn(Optional.of(mockedComment))
                    .when(commentRepository)
                    .findById(id);

            CommentResponse actualValue = commentService.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindNewsByNewsIdWithComments {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() {
            NewsResponse mockedNewsResponse = NewsResponseTestBuilder.aNewsResponse().build();
            long id = mockedNewsResponse.getId();
            NewsWithCommentsResponse expectedValue = NewsWithCommentsResponseTestBuilder
                    .aNewsWithCommentsResponse().build();
            Comment mockedComment = CommentTestBuilder.aComment().build();
            CommentResponse mockedCommentResponse = CommentResponseTestBuilder.aCommentResponse().build();
            Pageable mockedPageable = PageRequest.of(1, 2);

            doReturn(mockedNewsResponse)
                    .when(newsService)
                    .findById(id);

            doReturn(List.of(mockedComment))
                    .when(commentRepository)
                    .findAllByNewsId(id, mockedPageable);

            doReturn(List.of(mockedCommentResponse))
                    .when(commentMapper)
                    .toResponses(List.of(mockedComment));

            doReturn(expectedValue)
                    .when(commentMapper)
                    .toNewsWithCommentsResponse(mockedNewsResponse, List.of(mockedCommentResponse));

            NewsWithCommentsResponse actualValue = commentService.findNewsByNewsIdWithComments(id, mockedPageable);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should return NewsWithCommentsResponse with List of Comments size 2")
        void testShouldReturnNewsWithCommentsResponseWithListOfSizeTwo() {
            NewsResponse mockedNewsResponse = NewsResponseTestBuilder.aNewsResponse().build();
            long id = mockedNewsResponse.getId();
            NewsWithCommentsResponse mockedNewsWithComments = NewsWithCommentsResponseTestBuilder
                    .aNewsWithCommentsResponse().build();
            Comment mockedComment = CommentTestBuilder.aComment().build();
            CommentResponse mockedCommentResponse = CommentResponseTestBuilder.aCommentResponse().build();
            Pageable mockedPageable = PageRequest.of(1, 2);
            int expectedSize = 2;

            doReturn(mockedNewsResponse)
                    .when(newsService)
                    .findById(id);

            doReturn(List.of(mockedComment))
                    .when(commentRepository)
                    .findAllByNewsId(id, mockedPageable);

            doReturn(List.of(mockedCommentResponse))
                    .when(commentMapper)
                    .toResponses(List.of(mockedComment));

            doReturn(mockedNewsWithComments)
                    .when(commentMapper)
                    .toNewsWithCommentsResponse(mockedNewsResponse, List.of(mockedCommentResponse));

            NewsWithCommentsResponse actualValue = commentService.findNewsByNewsIdWithComments(id, mockedPageable);

            assertThat(actualValue.getCommentsList()).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return NewsWithCommentsResponse with List that contains expected value")
        void testShouldReturnNewsWithCommentsResponseWithListThatContainsExpectedValue() {
            NewsResponse mockedNewsResponse = NewsResponseTestBuilder.aNewsResponse().build();
            long id = mockedNewsResponse.getId();
            NewsWithCommentsResponse mockedNewsWithComments = NewsWithCommentsResponseTestBuilder
                    .aNewsWithCommentsResponse().build();
            Comment mockedComment = CommentTestBuilder.aComment().build();
            CommentResponse mockedCommentResponse = CommentResponseTestBuilder.aCommentResponse().build();
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse().build();
            Pageable mockedPageable = PageRequest.of(1, 2);

            doReturn(mockedNewsResponse)
                    .when(newsService)
                    .findById(id);

            doReturn(List.of(mockedComment))
                    .when(commentRepository)
                    .findAllByNewsId(id, mockedPageable);

            doReturn(List.of(mockedCommentResponse))
                    .when(commentMapper)
                    .toResponses(List.of(mockedComment));

            doReturn(mockedNewsWithComments)
                    .when(commentMapper)
                    .toNewsWithCommentsResponse(mockedNewsResponse, List.of(mockedCommentResponse));

            NewsWithCommentsResponse actualValue = commentService.findNewsByNewsIdWithComments(id, mockedPageable);

            assertThat(actualValue.getCommentsList()).contains(expectedValue);
        }

        @Test
        @DisplayName("test should return NewsWithCommentsResponse with empty List of Comments")
        void testShouldReturnNewsWithCommentsResponseWithEmptyListOfComments() {
            NewsResponse mockedNewsResponse = NewsResponseTestBuilder.aNewsResponse().build();
            Pageable mockedPageable = PageRequest.of(1, 2);
            Comment mockedComment = CommentTestBuilder.aComment().build();
            NewsWithCommentsResponse mockedNewsWithComments = NewsWithCommentsResponseTestBuilder
                    .aNewsWithCommentsResponse().withComments(List.of()).build();
            long id = mockedComment.getId();

            doReturn(mockedNewsResponse)
                    .when(newsService)
                    .findById(id);

            doReturn(List.of())
                    .when(commentRepository)
                    .findAllByNewsId(id, mockedPageable);

            doReturn(mockedNewsWithComments)
                    .when(commentMapper)
                    .toNewsWithCommentsResponse(mockedNewsResponse, List.of());

            NewsWithCommentsResponse actualValue = commentService.findNewsByNewsIdWithComments(id, mockedPageable);

            assertThat(actualValue.getCommentsList()).isEmpty();
        }

    }

    @Nested
    class FindAllByMatchingTextParams {

        @Test
        @DisplayName("test should return List of size 1")
        void testShouldReturnListOfSizeOne() {
            CommentResponse mockedCommentResponse = CommentResponseTestBuilder.aCommentResponse().build();
            Comment mockedComment = CommentTestBuilder.aComment().build();
            ExampleMatcher exampleMatcher = ExampleMatcherTestBuilder.aExampleMatcher().build();
            Example<Comment> mockedCommentExample = Example.of(mockedComment, exampleMatcher);
            Page<Comment> mockedPage = new PageImpl<>(List.of(mockedComment));
            Pageable mockedPageable = PageRequest.of(1, 2);
            String text = "It's terrible what's going on...";
            String username = "Evlampia";

            int expectedSize = 1;

            doReturn(mockedComment)
                    .when(commentMapper)
                    .fromParams(text, username);

            doReturn(List.of(mockedCommentResponse))
                    .when(commentMapper)
                    .toResponses(List.of(mockedComment));

            doReturn(mockedPage)
                    .when(commentRepository)
                    .findAll(mockedCommentExample, mockedPageable);

            CommentResponseList actualValues = commentService.findAllByMatchingTextParams(text, username, mockedPageable);

            assertThat(actualValues.getCommentsList()).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return List that contains expected value")
        void testShouldReturnListThatContainsExpectedValue() {
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse().build();
            Comment mockedComment = CommentTestBuilder.aComment().build();
            ExampleMatcher exampleMatcher = ExampleMatcherTestBuilder.aExampleMatcher().build();
            Example<Comment> mockedCommentExample = Example.of(mockedComment, exampleMatcher);
            Page<Comment> mockedPage = new PageImpl<>(List.of(mockedComment));
            Pageable mockedPageable = PageRequest.of(1, 2);
            String text = "It's terrible what's going on...";
            String username = "Evlampia";

            doReturn(mockedComment)
                    .when(commentMapper)
                    .fromParams(text, username);

            doReturn(List.of(expectedValue))
                    .when(commentMapper)
                    .toResponses(List.of(mockedComment));

            doReturn(mockedPage)
                    .when(commentRepository)
                    .findAll(mockedCommentExample, mockedPageable);

            CommentResponseList actualValues = commentService.findAllByMatchingTextParams(text, username, mockedPageable);

            assertThat(actualValues.getCommentsList()).contains(expectedValue);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            Pageable mockedPageable = PageRequest.of(1, 2);
            Comment mockedComment = CommentTestBuilder.aComment().build();
            ExampleMatcher exampleMatcher = ExampleMatcherTestBuilder.aExampleMatcher().build();
            Example<Comment> mockedCommentExample = Example.of(mockedComment, exampleMatcher);
            String text = "It's terrible what's going on...";
            String username = "Evlampia";

            doReturn(mockedComment)
                    .when(commentMapper)
                    .fromParams(text, username);

            doReturn(Page.empty())
                    .when(commentRepository)
                    .findAll(mockedCommentExample, mockedPageable);

            CommentResponseList actualValues = commentService.findAllByMatchingTextParams(text, username, mockedPageable);

            assertThat(actualValues.getCommentsList()).isEmpty();
        }

    }

    @Nested
    class SaveTest {

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test should capture saved value")
        @MethodSource("ru.clevertec.newsservice.service.impl.CommentServiceImplTest#getArgumentsForSaveTest")
        void testShouldCaptureValue(Comment expectedValue) {
            CommentResponse mockedCommentResponse = CommentResponseTestBuilder.aCommentResponse()
                    .withId(expectedValue.getId())
                    .withUsername(expectedValue.getUsername())
                    .withText(expectedValue.getText())
                    .build();
            CommentWithNewsRequest mockedRequest = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            NewsResponse mockedNewsResponse = NewsResponseTestBuilder.aNewsResponse().build();
            News mockedNews = NewsTestBuilder.aNews().build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.SUBSCRIBER);

            doReturn(mockedNewsResponse)
                    .when(newsService)
                    .findById(mockedRequest.getNewsId());

            doReturn(expectedValue)
                    .when(commentMapper)
                    .fromWithNewsRequest(mockedRequest);

            doReturn(mockedNews)
                    .when(newsMapper)
                    .fromResponse(mockedNewsResponse);

            doReturn(expectedValue)
                    .when(commentRepository)
                    .save(expectedValue);

            doReturn(mockedCommentResponse)
                    .when(commentMapper)
                    .toResponse(expectedValue);

            commentService.save(mockedRequest, token);
            verify(commentRepository).save(captor.capture());

            Comment captorValue = captor.getValue();
            assertThat(captorValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class UpdateByIdTest {

        @Test
        @DisplayName("test should return updated value")
        void testShouldReturnUpdatedTagDto() {
            Comment mockedComment = CommentTestBuilder.aComment().build();
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse().build();
            CommentRequest mockedCommentRequest = CommentRequestTestBuilder.aCommentRequest().build();
            long id = mockedComment.getId();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.SUBSCRIBER);

            doReturn(Optional.of(mockedComment))
                    .when(commentRepository)
                    .findById(id);

            doReturn(mockedComment)
                    .when(commentRepository)
                    .saveAndFlush(mockedComment);

            doReturn(expectedValue)
                    .when(commentMapper)
                    .toResponse(mockedComment);

            CommentResponse actualValue = commentService.updateById(id, mockedCommentRequest, token);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw NoSuchCommentException")
        void testShouldThrowNoSuchCommentException() {
            CommentRequest mockedCommentRequest = CommentRequestTestBuilder.aCommentRequest().build();
            long id = 2L;
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.SUBSCRIBER);

            doThrow(new NoSuchCommentException(""))
                    .when(commentRepository)
                    .findById(id);

            assertThrows(NoSuchCommentException.class, () -> commentService.updateById(id, mockedCommentRequest, token));
        }

        @Test
        @DisplayName("test should throw NoSuchCommentException with expected message")
        void testShouldThrowNoSuchCommentExceptionWithExpectedMessage() {
            CommentRequest mockedCommentRequest = CommentRequestTestBuilder.aCommentRequest().build();
            long id = 1L;
            String expectedMessage = "There is no Comment with ID " + id + " to update";
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.SUBSCRIBER);

            Exception exception = assertThrows(NoSuchCommentException.class,
                    () -> commentService.updateById(id, mockedCommentRequest, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    class DeleteByIdTest {

        @Test
        @DisplayName("test should return expected DeleteResponse")
        void testShouldReturnExpectedDeleteResponse() {
            Comment mockedComment = CommentTestBuilder.aComment().build();
            long id = mockedComment.getId();
            DeleteResponse expectedValue = DeleteResponse.newBuilder()
                    .setMessage("Comment with ID " + id + " was successfully deleted")
                    .build();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.SUBSCRIBER);

            doReturn(Optional.of(mockedComment))
                    .when(commentRepository)
                    .findById(id);

            doNothing()
                    .when(commentRepository)
                    .delete(mockedComment);

            DeleteResponse actualValue = commentService.deleteById(id, token);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should invoke method 1 time")
        void testShouldInvokeOneTime() {
            Comment mockedComment = CommentTestBuilder.aComment().build();
            long id = mockedComment.getId();
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.SUBSCRIBER);

            doReturn(Optional.of(mockedComment))
                    .when(commentRepository)
                    .findById(id);

            doNothing()
                    .when(commentRepository)
                    .delete(mockedComment);

            commentService.deleteById(id, token);

            verify(commentRepository, times(1))
                    .delete(mockedComment);
        }

        @Test
        @DisplayName("test should throw NoSuchCommentException")
        void testShouldThrowNoSuchCommentException() {
            long id = 2L;
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.SUBSCRIBER);

            doThrow(new NoSuchCommentException(""))
                    .when(commentRepository)
                    .findById(id);

            assertThrows(NoSuchCommentException.class, () -> commentService.deleteById(id, token));
        }

        @Test
        @DisplayName("test should throw NoSuchCommentException with expected message")
        void testShouldThrowNoSuchCommentExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "There is no Comment with ID " + id + " to delete";
            TokenValidationResponse response = TokenValidationResponseTestBuilder.aTokenValidationResponse().build();
            String token = "jwt";

            doReturn(response)
                    .when(authenticationService)
                    .checkTokenValidationForRole(token, Role.SUBSCRIBER);

            Exception exception = assertThrows(NoSuchCommentException.class, () -> commentService.deleteById(id, token));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    private static Stream<Arguments> getArgumentsForSaveTest() {
        return Stream.of(
                Arguments.of(CommentTestBuilder.aComment().build()),
                Arguments.of(CommentTestBuilder.aComment().withId(2L)
                        .withUsername("George")
                        .build()),
                Arguments.of(CommentTestBuilder.aComment().withId(3L)
                        .withUsername("Sally")
                        .withText("Wow!!!")
                        .build()));
    }

}
