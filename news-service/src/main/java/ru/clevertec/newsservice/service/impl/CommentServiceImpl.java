package ru.clevertec.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsservice.aop.annotation.GetCacheable;
import ru.clevertec.newsservice.aop.annotation.PutCacheable;
import ru.clevertec.newsservice.aop.annotation.RemoveCacheable;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.comment.CommentRequest;
import ru.clevertec.newsservice.dto.comment.CommentResponse;
import ru.clevertec.newsservice.dto.comment.CommentUpdateRequest;
import ru.clevertec.newsservice.dto.comment.CommentWithNewsRequest;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.newsservice.dto.news.NewsWithCommentsResponse;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchCommentException;
import ru.clevertec.newsservice.mapper.CommentMapper;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.model.Comment;
import ru.clevertec.newsservice.model.News;
import ru.clevertec.newsservice.repository.CommentRepository;
import ru.clevertec.newsservice.service.CommentService;
import ru.clevertec.newsservice.service.NewsService;

import java.util.List;

/**
 * The CommentServiceImpl class implements CommentService and provides the implementation for CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final NewsService newsService;
    private final NewsMapper newsMapper;

    /**
     * Finds one {@link Comment} by ID.
     *
     * @param id the ID of the Comment.
     * @return {@link CommentResponse} with the specified ID and mapped from Comment entity.
     * @throws NoSuchCommentException if Comment is not exists by finding it by ID.
     */
    @Override
    @GetCacheable
    public CommentResponse findById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::toResponse)
                .orElseThrow(() -> new NoSuchCommentException("Comment with ID " + id + " does not exist"));
    }

    /**
     * Finds {@link News} with {@link Comment}s and pagination.
     *
     * @param newsId   the ID of the News.
     * @param pageable {@link Pageable} Comments will be sorted by its parameters and divided into pages.
     * @return {@link NewsWithCommentsResponse} that contains one News and its Comments.
     */
    @Override
    public NewsWithCommentsResponse findNewsByNewsIdWithComments(Long newsId, Pageable pageable) {
        NewsResponse newsResponse = newsService.findById(newsId);
        List<Comment> comments = commentRepository.findAllByNewsId(newsId, pageable);
        return commentMapper.toWithCommentsResponse(newsResponse, comments);
    }


    /**
     * Finds all {@link Comment} by matching it through {@link ExampleMatcher} with pagination.
     *
     * @param commentRequest the {@link CommentRequest} which will be mapped to {@link CommentResponse}.
     * @param pageable       {@link Pageable} Comments will be sorted by its parameters and divided into pages.
     * @return sorted by pageable, filtered by ExampleMatcher and mapped from entity to dto list of all CommentResponse.
     */
    @Override
    public List<CommentResponse> findAllByMatchingTextParams(CommentRequest commentRequest, Pageable pageable) {
        Comment comment = commentMapper.fromRequest(commentRequest);
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Comment> commentExample = Example.of(comment, exampleMatcher);
        return commentMapper.toResponses(commentRepository.findAll(commentExample, pageable).stream().toList());
    }

    /**
     * Saves one {@link Comment} and links it to the {@link News}.
     *
     * @param commentWithNewsRequest the {@link CommentWithNewsRequest} which will be mapped to {@link CommentResponse}
     *                               and saved in database by repository.
     * @return the CommentResponse which was mapped from saved Comment entity.
     */
    @Override
    @PutCacheable
    @Transactional
    public CommentResponse save(CommentWithNewsRequest commentWithNewsRequest) {
        NewsResponse newsResponse = newsService.findById(commentWithNewsRequest.newsId());
        Comment comment = commentMapper.fromWithNewsRequest(commentWithNewsRequest);
        News news = newsMapper.fromResponse(newsResponse);
        comment.setNews(news);
        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponse(saved);
    }

    /**
     * Updates one {@link Comment} by ID.
     *
     * @param id             the ID of the Comment.
     * @param commentUpdateRequest the {@link CommentUpdateRequest}.
     * @return the CommentResponse which was mapped from updated Comment entity.
     * @throws NoSuchCommentException if Comment is not exists by finding it by ID.
     */
    @Override
    @PutCacheable
    @Transactional
    public CommentResponse updateById(Long id, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchCommentException("There is no Comment with ID " + id + " to update"));
        comment.setText(commentUpdateRequest.text());
        comment.setUsername(commentUpdateRequest.username());
        Comment saved = commentRepository.saveAndFlush(comment);
        return commentMapper.toResponse(saved);
    }

    /**
     * Deletes one {@link Comment} by ID.
     *
     * @param id the ID of the Comment.
     * @return the {@link DeleteResponse} with message that Comment was deleted.
     * @throws NoSuchCommentException if Comment is not exists by finding it by ID.
     */
    @Override
    @Transactional
    @RemoveCacheable
    public DeleteResponse deleteById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchCommentException("There is no Comment with ID " + id + " to delete"));
        commentRepository.delete(comment);
        return new DeleteResponse("Comment with ID " + id + " was successfully deleted");
    }

}
