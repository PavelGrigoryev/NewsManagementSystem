package ru.clevertec.newsservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import ru.clevertec.newsservice.dto.comment.CommentResponse;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.newsservice.dto.news.NewsWithCommentsResponse;
import ru.clevertec.newsservice.dto.proto.CommentRequest;
import ru.clevertec.newsservice.dto.proto.CommentWithNewsRequest;
import ru.clevertec.newsservice.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {

    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponses(List<Comment> comments);

    @BeanMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    Comment fromParams(String text, String username);

    Comment fromRequest(CommentRequest commentRequest);

    @Mapping(target = "comments", source = "comments")
    NewsWithCommentsResponse toWithCommentsResponse(NewsResponse newsResponse, List<Comment> comments);

    Comment fromWithNewsRequest(CommentWithNewsRequest commentWithNewsRequest);

}
