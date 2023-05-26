package ru.clevertec.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsservice.dto.CommentResponse;
import ru.clevertec.newsservice.dto.NewsMatcherRequest;
import ru.clevertec.newsservice.dto.NewsResponse;
import ru.clevertec.newsservice.dto.NewsWithCommentsResponse;
import ru.clevertec.newsservice.exception.NoSuchNewsException;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.model.News;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.service.CommentService;
import ru.clevertec.newsservice.service.NewsService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final CommentService commentService;

    @Override
    public NewsResponse findById(Long id) {
        return newsRepository.findById(id)
                .map(newsMapper::toResponse)
                .orElseThrow(() -> new NoSuchNewsException("News with ID " + id + " does not exist"));
    }

    @Override
    public List<NewsResponse> findAll(Pageable pageable) {
        return newsMapper.toResponses(newsRepository.findAll(pageable));
    }

    @Override
    public NewsWithCommentsResponse findByIdWithComments(Long id, Pageable pageable) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new NoSuchNewsException("News with ID " + id + " does not exist"));
        List<CommentResponse> commentResponses = commentService.findAllByNewsId(id, pageable);
        return newsMapper.toWithCommentsResponse(news, commentResponses);
    }

    @Override
    public List<NewsResponse> findAllByMatchingTextParams(NewsMatcherRequest newsMatcherRequest, Pageable pageable) {
        News news = newsMapper.fromRequest(newsMatcherRequest);
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<News> newsExample = Example.of(news, exampleMatcher);
        return newsMapper.toResponses(newsRepository.findAll(newsExample, pageable));
    }

}
