package ru.clevertec.newsservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.newsservice.dto.comment.CommentRequest;
import ru.clevertec.newsservice.dto.comment.CommentResponse;
import ru.clevertec.newsservice.dto.comment.CommentWithNewsRequest;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.newsservice.dto.news.NewsWithCommentsResponse;
import ru.clevertec.newsservice.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {

    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponses(List<Comment> comments);

    Comment fromRequest(CommentRequest commentRequest);

    @Mapping(target = "comments", source = "comments")
    NewsWithCommentsResponse toWithCommentsResponse(NewsResponse newsResponse, List<Comment> comments);

    Comment fromWithNewsRequest(CommentWithNewsRequest commentWithNewsRequest);

}
