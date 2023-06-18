package ru.clevertec.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchNewsException;
import ru.clevertec.newsservice.aop.annotation.GetCacheable;
import ru.clevertec.newsservice.aop.annotation.PutCacheable;
import ru.clevertec.newsservice.aop.annotation.RemoveCacheable;
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
import ru.clevertec.newsservice.service.NewsService;

import java.util.List;

/**
 * The NewsServiceImpl class implements NewsService and provides the implementation for CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final AuthenticationService authenticationService;

    /**
     * Finds one {@link News} by ID.
     *
     * @param id the ID of the News.
     * @return {@link NewsResponse} with the specified ID and mapped from News entity.
     * @throws NoSuchNewsException if News is not exists by finding it by ID.
     */
    @Override
    @GetCacheable
    @Cacheable(value = "news")
    public NewsResponse findById(Long id) {
        return newsRepository.findById(id)
                .map(newsMapper::toResponse)
                .orElseThrow(() -> new NoSuchNewsException("News with ID " + id + " does not exist"));
    }

    /**
     * Finds all {@link News} with pagination.
     *
     * @param pageable the {@link Pageable} News will be sorted by its parameters and divided into pages.
     * @return a sorted by pageable and mapped from entity to dto {@link NewsResponseList}.
     */
    @Override
    public NewsResponseList findAll(Pageable pageable) {
        List<NewsResponse> responses = newsMapper.toResponses(newsRepository.findAll(pageable));
        return NewsResponseList.newBuilder()
                .addAllNews(responses)
                .build();
    }

    /**
     * Finds all {@link News} by matching it through {@link ExampleMatcher} with pagination.
     *
     * @param title    the title to match against News.
     * @param text     the text to match against News.
     * @param pageable {@link Pageable} News will be sorted by its parameters and divided into pages.
     * @return sorted by pageable, filtered by ExampleMatcher and mapped from entity to dto {@link NewsResponseList}.
     */
    @Override
    public NewsResponseList findAllByMatchingTextParams(String title, String text, Pageable pageable) {
        News news = newsMapper.fromParams(title, text);
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<News> newsExample = Example.of(news, exampleMatcher);
        List<NewsResponse> responses = newsMapper.toResponses(newsRepository.findAll(newsExample, pageable));
        return NewsResponseList.newBuilder()
                .addAllNews(responses)
                .build();
    }

    /**
     * Saves one {@link News}.
     *
     * @param newsRequest the {@link NewsRequest} which will be mapped to {@link NewsResponse} and saved in database
     *                    by repository.
     * @param token       jwt token.
     * @return the NewsResponse which was mapped from saved News entity.
     */
    @Override
    @PutCacheable
    @Transactional
    @CachePut(value = "news", key = "#result.getId()")
    public NewsResponse save(NewsRequest newsRequest, String token) {
        TokenValidationResponse response = authenticationService.checkTokenValidationForRole(token, Role.JOURNALIST);
        News news = newsMapper.fromRequest(newsRequest);
        news.setEmail(response.getEmail());
        News saved = newsRepository.save(news);
        return newsMapper.toResponse(saved);
    }

    /**
     * Updates one {@link News} by ID.
     *
     * @param id          the ID of the News.
     * @param newsRequest the {@link NewsRequest}.
     * @param token       jwt token.
     * @return the NewsResponse which was mapped from updated News entity.
     * @throws NoSuchNewsException if News is not exists by finding it by ID.
     */
    @Override
    @PutCacheable
    @Transactional
    @CachePut(value = "news", key = "#result.getId()")
    public NewsResponse updateById(Long id, NewsRequest newsRequest, String token) {
        TokenValidationResponse response = authenticationService.checkTokenValidationForRole(token, Role.JOURNALIST);
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new NoSuchNewsException("There is no News with ID " + id + " to update"));
        authenticationService.isObjectOwnedByEmailAndRole(
                response.getRole(), Role.JOURNALIST, response.getEmail(), news.getEmail());
        news.setTitle(newsRequest.getTitle());
        news.setText(newsRequest.getText());
        news.setEmail(response.getEmail());
        News saved = newsRepository.saveAndFlush(news);
        return newsMapper.toResponse(saved);
    }

    /**
     * Deletes one {@link News} by ID and related Comments.
     *
     * @param id    the ID of the News.
     * @param token jwt token.
     * @return the {@link DeleteResponse} with message that News was deleted.
     * @throws NoSuchNewsException if News is not exists by finding it by ID.
     */
    @Override
    @Transactional
    @RemoveCacheable
    @CacheEvict(value = "news", key = "#id")
    public DeleteResponse deleteById(Long id, String token) {
        TokenValidationResponse response = authenticationService.checkTokenValidationForRole(token, Role.JOURNALIST);
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new NoSuchNewsException("There is no News with ID " + id + " to delete"));
        authenticationService.isObjectOwnedByEmailAndRole(
                response.getRole(), Role.JOURNALIST, response.getEmail(), news.getEmail());
        newsRepository.delete(news);
        return DeleteResponse.newBuilder().setMessage("News with ID " + id + " was successfully deleted").build();
    }

}
