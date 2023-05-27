package ru.clevertec.newsservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsservice.aop.annotation.Loggable;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.news.NewsRequest;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.newsservice.service.NewsService;

import java.util.List;

@Loggable
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(newsService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<NewsResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(newsService.findAll(pageable));
    }

    @GetMapping("/params")
    public ResponseEntity<List<NewsResponse>> findAllByMatchingTextParams(NewsRequest newsRequest,
                                                                          Pageable pageable) {
        return ResponseEntity.ok(newsService.findAllByMatchingTextParams(newsRequest, pageable));
    }

    @PostMapping
    public ResponseEntity<NewsResponse> save(@RequestBody @Valid NewsRequest newsRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(newsService.save(newsRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> updateById(@PathVariable @Positive Long id,
                                                   @RequestBody @Valid NewsRequest newsRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(newsService.updateById(id, newsRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(newsService.deleteById(id));
    }

}
