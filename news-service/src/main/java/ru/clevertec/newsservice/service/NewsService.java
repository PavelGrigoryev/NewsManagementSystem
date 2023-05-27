package ru.clevertec.newsservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.news.NewsRequest;
import ru.clevertec.newsservice.dto.news.NewsResponse;

import java.util.List;

public interface NewsService {

    NewsResponse findById(Long id);

    List<NewsResponse> findAll(Pageable pageable);

    List<NewsResponse> findAllByMatchingTextParams(NewsRequest newsRequest, Pageable pageable);

    NewsResponse save(NewsRequest newsRequest);

    NewsResponse updateById(Long id, NewsRequest newsRequest);

    DeleteResponse deleteById(Long id);

}
