package ru.clevertec.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsservice.dto.CommentResponse;
import ru.clevertec.newsservice.exception.NoSuchCommentException;
import ru.clevertec.newsservice.mapper.CommentMapper;
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

}
