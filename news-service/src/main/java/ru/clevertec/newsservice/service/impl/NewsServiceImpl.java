package ru.clevertec.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsservice.dto.NewsResponse;
import ru.clevertec.newsservice.dto.NewsWithCommentsResponse;
import ru.clevertec.newsservice.exception.NoSuchNewsException;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.service.NewsService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Override
    public NewsResponse findById(Long id) {
        return newsRepository.findById(id)
                .map(newsMapper::toResponse)
                .orElseThrow(() -> new NoSuchNewsException("News with ID " + id + " does not exist"));
    }

    @Override
    public NewsWithCommentsResponse findByIdWithComments(Long id) {
        return newsRepository.findByIdWithComments(id)
                .map(newsMapper::toWithCommentsResponse)
                .orElseThrow(() -> new NoSuchNewsException("News with ID " + id + " does not exist"));
    }

}
