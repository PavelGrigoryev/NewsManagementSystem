package ru.clevertec.newsservice.service;

import ru.clevertec.newsservice.dto.NewsResponse;
import ru.clevertec.newsservice.dto.NewsWithCommentsResponse;

public interface NewsService {

    NewsResponse findById(Long id);

    NewsWithCommentsResponse findByIdWithComments(Long id);

}
