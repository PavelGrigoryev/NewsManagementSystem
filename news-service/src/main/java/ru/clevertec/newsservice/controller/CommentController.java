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
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.comment.CommentRequest;
import ru.clevertec.newsservice.dto.comment.CommentResponse;
import ru.clevertec.newsservice.dto.comment.CommentWithNewsRequest;
import ru.clevertec.newsservice.dto.news.NewsWithCommentsResponse;
import ru.clevertec.newsservice.service.CommentService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @GetMapping("/news/{newsId}")
    public ResponseEntity<NewsWithCommentsResponse> findNewsByNewsIdWithComments(@PathVariable @Positive Long newsId,
                                                                                 Pageable pageable) {
        return ResponseEntity.ok(commentService.findNewsByNewsIdWithComments(newsId, pageable));
    }

    @GetMapping("/params")
    public ResponseEntity<List<CommentResponse>> findAllByMatchingTextParams(CommentRequest commentRequest,
                                                                             Pageable pageable) {
        return ResponseEntity.ok(commentService.findAllByMatchingTextParams(commentRequest, pageable));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> save(@RequestBody @Valid CommentWithNewsRequest commentWithNewsRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(commentWithNewsRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateById(@PathVariable @Positive Long id,
                                                      @RequestBody @Valid CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.updateById(id, commentRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(commentService.deleteById(id));
    }

}
