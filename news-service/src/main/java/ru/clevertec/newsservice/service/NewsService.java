package ru.clevertec.newsservice.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.newsservice.dto.NewsMatcherRequest;
import ru.clevertec.newsservice.dto.NewsResponse;
import ru.clevertec.newsservice.dto.NewsWithCommentsResponse;

import java.util.List;

public interface NewsService {

    NewsResponse findById(Long id);

    List<NewsResponse> findAll(Pageable pageable);

    NewsWithCommentsResponse findByIdWithComments(Long id, Pageable pageable);

    List<NewsResponse> findAllByMatchingTextParams(NewsMatcherRequest newsMatcherRequest, Pageable pageable);

}
