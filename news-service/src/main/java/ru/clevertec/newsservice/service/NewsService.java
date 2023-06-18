package ru.clevertec.newsservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.newsservice.dto.proto.DeleteResponse;
import ru.clevertec.newsservice.dto.proto.NewsRequest;
import ru.clevertec.newsservice.dto.proto.NewsResponse;
import ru.clevertec.newsservice.dto.proto.NewsResponseList;

/**
 * The NewsService interface provides the implementation for CRUD operations.
 */
public interface NewsService {

    NewsResponse findById(Long id);

    NewsResponseList findAll(Pageable pageable);

    NewsResponseList findAllByMatchingTextParams(String title, String text, Pageable pageable);

    NewsResponse save(NewsRequest newsRequest, String token);

    NewsResponse updateById(Long id, NewsRequest newsRequest, String token);

    DeleteResponse deleteById(Long id, String token);

}
