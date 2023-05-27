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
    public List<NewsResponse> findAll(Pageable pageable) {
        return newsMapper.toResponses(newsRepository.findAll(pageable));
    }

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

    @Override
    @Transactional
    public NewsResponse save(NewsRequest newsRequest) {
        News news = newsMapper.fromRequest(newsRequest);
        News saved = newsRepository.save(news);
        return newsMapper.toResponse(saved);
    }

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

    @Override
    @Transactional
    public DeleteResponse deleteById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new NoSuchNewsException("There is no News with ID " + id + " to delete"));
        newsRepository.delete(news);
        return new DeleteResponse("News with ID " + id + " was successfully deleted");
    }

}
