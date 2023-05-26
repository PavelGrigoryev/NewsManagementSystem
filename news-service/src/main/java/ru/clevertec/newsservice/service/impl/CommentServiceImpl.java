package ru.clevertec.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.comment.CommentRequest;
import ru.clevertec.newsservice.dto.comment.CommentResponse;
import ru.clevertec.newsservice.exception.NoSuchCommentException;
import ru.clevertec.newsservice.mapper.CommentMapper;
import ru.clevertec.newsservice.model.Comment;
import ru.clevertec.newsservice.repository.CommentRepository;
import ru.clevertec.newsservice.service.CommentService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse findById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::toResponse)
                .orElseThrow(() -> new NoSuchCommentException("Comment with ID " + id + " does not exist"));
    }

    @Override
    public List<CommentResponse> findAllByNewsId(Long newsId, Pageable pageable) {
        return commentMapper.toResponses(commentRepository.findAllByNewsId(newsId, pageable));
    }

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

    @Override
    @Transactional
    public CommentResponse save(CommentRequest commentRequest) {
        Comment comment = commentMapper.fromRequest(commentRequest);
        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CommentResponse updateById(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchCommentException("There is no Comment with ID " + id + " to update"));
        comment.setText(commentRequest.text());
        comment.setUsername(commentRequest.username());
        Comment saved = commentRepository.saveAndFlush(comment);
        return commentMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public DeleteResponse deleteById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchCommentException("There is no Comment with ID " + id + " to delete"));
        commentRepository.delete(comment);
        return new DeleteResponse("Comment with ID " + id + " was successfully deleted");
    }

}
