package ru.clevertec.newsservice.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.newsservice.exception.NoSuchNewsException;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.model.News;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.util.testbuilder.NewsResponseTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl newsService;
    @Mock
    private NewsRepository newsRepository;
    @Mock
    private NewsMapper newsMapper;
    private final static NewsResponseTestBuilder TEST_BUILDER = NewsResponseTestBuilder.aNews();

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
            NewsResponse expectedValue = TEST_BUILDER.build();
            News mockedNews = new News();
            long id = expectedValue.id();

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
            NewsResponse mockedNewsResponse = TEST_BUILDER.build();
            News mockedNews = new News();
            Page<News> mockedPage = new PageImpl<>(List.of(mockedNews));
            Pageable mockedPageable = PageRequest.of(1,2);
            int expectedSize = 1;

            doReturn(List.of(mockedNewsResponse))
                    .when(newsMapper)
                    .toResponses(mockedPage);

            doReturn(mockedPage)
                    .when(newsRepository)
                    .findAll(mockedPageable);

            List<NewsResponse> actualValues = newsService.findAll(mockedPageable);
            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return List that contains expected value")
        void testShouldReturnListThatContainsExpectedValue() {
            NewsResponse expectedValue = TEST_BUILDER.build();
            News mockedNews = new News();
            Page<News> mockedPage = new PageImpl<>(List.of(mockedNews));
            Pageable mockedPageable = PageRequest.of(1,2);

            doReturn(List.of(expectedValue))
                    .when(newsMapper)
                    .toResponses(mockedPage);

            doReturn(mockedPage)
                    .when(newsRepository)
                    .findAll(mockedPageable);

            List<NewsResponse> actualValues = newsService.findAll(mockedPageable);
            assertThat(actualValues).contains(expectedValue);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            Pageable mockedPageable = PageRequest.of(1,2);

            doReturn(Page.empty())
                    .when(newsRepository)
                    .findAll(mockedPageable);

            List<NewsResponse> actualValues = newsService.findAll(mockedPageable);

            assertThat(actualValues).isEmpty();
        }

    }

}
