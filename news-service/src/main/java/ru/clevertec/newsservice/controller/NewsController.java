package ru.clevertec.newsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.loggingstarter.annotation.Loggable;
import ru.clevertec.newsservice.controller.openapi.NewsOpenApi;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.news.NewsRequest;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.newsservice.service.NewsService;

import java.util.List;

@Loggable
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController implements NewsOpenApi {

    private final NewsService newsService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.findById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<NewsResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(newsService.findAll(pageable));
    }


    @Override
    @GetMapping("/params")
    public ResponseEntity<List<NewsResponse>> findAllByMatchingTextParams(NewsRequest newsRequest,
                                                                          Pageable pageable) {
        return ResponseEntity.ok(newsService.findAllByMatchingTextParams(newsRequest, pageable));
    }

    @Override
    @PostMapping
    public ResponseEntity<NewsResponse> save(@RequestBody NewsRequest newsRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(newsService.save(newsRequest));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> updateById(@PathVariable Long id,
                                                   @RequestBody NewsRequest newsRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(newsService.updateById(id, newsRequest));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.deleteById(id));
    }

}
