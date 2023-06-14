package ru.clevertec.newsservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.news.NewsRequest;
import ru.clevertec.newsservice.dto.news.NewsResponse;

import java.util.List;

/**
 * The NewsService interface provides the implementation for CRUD operations.
 */
public interface NewsService {

    NewsResponse findById(Long id);

    List<NewsResponse> findAll(Pageable pageable);

    List<NewsResponse> findAllByMatchingTextParams(NewsRequest newsRequest, Pageable pageable);

    NewsResponse save(NewsRequest newsRequest, String token);

    NewsResponse updateById(Long id, NewsRequest newsRequest, String token);

    DeleteResponse deleteById(Long id, String token);

}
