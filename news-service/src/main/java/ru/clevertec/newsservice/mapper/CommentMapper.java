package ru.clevertec.newsservice.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.newsservice.dto.comment.CommentRequest;
import ru.clevertec.newsservice.dto.comment.CommentResponse;
import ru.clevertec.newsservice.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {

    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponses(List<Comment> comments);

    Comment fromRequest(CommentRequest commentRequest);

}
