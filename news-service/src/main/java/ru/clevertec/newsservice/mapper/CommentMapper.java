package ru.clevertec.newsservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import ru.clevertec.newsservice.dto.proto.CommentResponse;
import ru.clevertec.newsservice.dto.proto.CommentWithNewsRequest;
import ru.clevertec.newsservice.dto.proto.NewsResponse;
import ru.clevertec.newsservice.dto.proto.NewsWithCommentsResponse;
import ru.clevertec.newsservice.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {

    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponses(List<Comment> comments);

    @BeanMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    Comment fromParams(String text, String username);

    Comment fromWithNewsRequest(CommentWithNewsRequest commentWithNewsRequest);

    /**
     * Converts a {@link NewsResponse} and a list of {@link CommentResponse} into a {@link NewsWithCommentsResponse}.
     *
     * @param response  the NewsResponse to convert.
     * @param responses the list of CommentResponse to add to the NewsWithCommentsResponse.
     * @return a NewsWithCommentsResponse.
     */
    default NewsWithCommentsResponse toNewsWithCommentsResponse(NewsResponse response, List<CommentResponse> responses) {
        return NewsWithCommentsResponse.newBuilder()
                .setId(response.getId())
                .setTime(response.getTime())
                .setTitle(response.getTitle())
                .setText(response.getText())
                .setEmail(response.getEmail())
                .addAllCommentsResponses(responses)
                .build();
    }

}
