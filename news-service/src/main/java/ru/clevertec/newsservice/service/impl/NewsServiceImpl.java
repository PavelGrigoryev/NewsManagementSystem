package ru.clevertec.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.news.NewsRequest;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.newsservice.exception.NoSuchNewsException;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.model.News;
import ru.clevertec.newsservice.repository.NewsRepository;
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

    /**
     * Finds one {@link News} by ID.
     *
     * @param id the ID of the News.
     * @return {@link NewsResponse} with the specified ID and mapped from News entity.
     * @throws NoSuchNewsException if News is not exists by finding it by ID.
     */
    @Override
    public NewsResponse findById(Long id) {
        return newsRepository.findById(id)
                .map(newsMapper::toResponse)
                .orElseThrow(() -> new NoSuchNewsException("News with ID " + id + " does not exist"));
    }

    /**
     * Finds all {@link News} with pagination.
     *
     * @param pageable the {@link Pageable} News will be sorted by its parameters and divided into pages.
     * @return a sorted by pageable and mapped from entity to dto list of all News.
     */
    @Override
    public List<NewsResponse> findAll(Pageable pageable) {
        return newsMapper.toResponses(newsRepository.findAll(pageable));
    }

    /**
     * Finds all {@link News} by matching it through {@link ExampleMatcher} with pagination.
     *
     * @param newsRequest the {@link NewsRequest} which will be mapped to {@link NewsResponse}.
     * @param pageable    {@link Pageable} News will be sorted by its parameters and divided into pages.
     * @return sorted by pageable, filtered by ExampleMatcher and mapped from entity to dto list of all NewsResponse.
     */
    @Override
    public List<NewsResponse> findAllByMatchingTextParams(NewsRequest newsRequest, Pageable pageable) {
        News news = newsMapper.fromRequest(newsRequest);
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<News> newsExample = Example.of(news, exampleMatcher);
        return newsMapper.toResponses(newsRepository.findAll(newsExample, pageable));
    }

    /**
     * Saves one {@link News}.
     *
     * @param newsRequest the {@link NewsRequest} which will be mapped to {@link NewsResponse} and saved in database
     *                    by repository.
     * @return the NewsResponse which was mapped from saved News entity.
     */
    @Override
    @Transactional
    public NewsResponse save(NewsRequest newsRequest) {
        News news = newsMapper.fromRequest(newsRequest);
        News saved = newsRepository.save(news);
        return newsMapper.toResponse(saved);
    }

    /**
     * Updates one {@link News} by ID.
     *
     * @param id          the ID of the News.
     * @param newsRequest the {@link NewsRequest} which will be mapped to {@link NewsResponse}.
     * @return the NewsResponse which was mapped from updated News entity.
     * @throws NoSuchNewsException if News is not exists by finding it by ID.
     */
    @Override
    @Transactional
    public NewsResponse updateById(Long id, NewsRequest newsRequest) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new NoSuchNewsException("There is no News with ID " + id + " to update"));
        news.setTitle(newsRequest.title());
        news.setText(newsRequest.text());
        News saved = newsRepository.saveAndFlush(news);
        return newsMapper.toResponse(saved);
    }

    /**
     * Deletes one {@link News} by ID.
     *
     * @param id the ID of the News.
     * @return the {@link DeleteResponse} with message that News was deleted.
     * @throws NoSuchNewsException if News is not exists by finding it by ID.
     */
    @Override
    @Transactional
    public DeleteResponse deleteById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new NoSuchNewsException("There is no News with ID " + id + " to delete"));
        newsRepository.delete(news);
        return new DeleteResponse("News with ID " + id + " was successfully deleted");
    }

}
