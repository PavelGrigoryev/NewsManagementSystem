package ru.clevertec.newsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsservice.dto.NewsResponse;
import ru.clevertec.newsservice.dto.NewsWithCommentsResponse;
import ru.clevertec.newsservice.service.NewsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.findById(id));
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<NewsWithCommentsResponse> findByIdWithComments(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.findByIdWithComments(id));
    }

}
