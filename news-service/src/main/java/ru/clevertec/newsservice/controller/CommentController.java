package ru.clevertec.newsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.loggingstarter.annotation.Loggable;
import ru.clevertec.newsservice.controller.openapi.CommentOpenApi;
import ru.clevertec.newsservice.dto.proto.DeleteResponse;
import ru.clevertec.newsservice.dto.proto.NewsWithCommentsResponse;
import ru.clevertec.newsservice.dto.proto.CommentRequest;
import ru.clevertec.newsservice.dto.proto.CommentResponse;
import ru.clevertec.newsservice.dto.proto.CommentResponseList;
import ru.clevertec.newsservice.dto.proto.CommentWithNewsRequest;
import ru.clevertec.newsservice.service.CommentService;
import ru.clevertec.newsservice.util.ProtobufValidator;

@Loggable
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController implements CommentOpenApi {

    private final CommentService commentService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @Override
    @GetMapping("/news/{newsId}")
    public ResponseEntity<NewsWithCommentsResponse> findNewsByNewsIdWithComments(@PathVariable Long newsId,
                                                                                 Pageable pageable) {
        return ResponseEntity.ok(commentService.findNewsByNewsIdWithComments(newsId, pageable));
    }

    @Override
    @GetMapping("/params")
    public ResponseEntity<CommentResponseList> findAllByMatchingTextParams(@RequestParam(required = false) String text,
                                                                           @RequestParam(required = false) String username,
                                                                           Pageable pageable) {
        return ResponseEntity.ok(commentService.findAllByMatchingTextParams(text, username, pageable));
    }

    @Override
    @PostMapping
    public ResponseEntity<CommentResponse> save(@RequestBody CommentWithNewsRequest commentWithNewsRequest,
                                                @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                String token) {
        ProtobufValidator.validateProto(commentWithNewsRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(commentWithNewsRequest, token));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateById(@PathVariable Long id,
                                                      @RequestBody CommentRequest commentRequest,
                                                      @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                      String token) {
        ProtobufValidator.validateProto(commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.updateById(id, commentRequest, token));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteById(@PathVariable Long id,
                                                     @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
                                                     String token) {
        return ResponseEntity.ok(commentService.deleteById(id, token));
    }

}
