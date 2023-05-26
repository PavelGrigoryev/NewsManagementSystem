package ru.clevertec.newsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsservice.dto.NewsMatcherRequest;
import ru.clevertec.newsservice.dto.NewsResponse;
import ru.clevertec.newsservice.dto.NewsWithCommentsResponse;
import ru.clevertec.newsservice.service.NewsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<NewsResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(newsService.findAll(pageable));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<NewsWithCommentsResponse> findByIdWithComments(@PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(newsService.findByIdWithComments(id, pageable));
    }

    @GetMapping("/params")
    public ResponseEntity<List<NewsResponse>> findAllByMatchingTextParams(NewsMatcherRequest newsMatcherRequest,
                                                                          Pageable pageable) {
        return ResponseEntity.ok(newsService.findAllByMatchingTextParams(newsMatcherRequest, pageable));
    }

}
