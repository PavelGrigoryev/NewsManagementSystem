package ru.clevertec.newsservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ru.clevertec.newsservice.dto.CommentResponse;
import ru.clevertec.newsservice.dto.NewsMatcherRequest;
import ru.clevertec.newsservice.dto.NewsResponse;
import ru.clevertec.newsservice.dto.NewsWithCommentsResponse;
import ru.clevertec.newsservice.model.News;

import java.util.List;

@Mapper
public interface NewsMapper {

    NewsResponse toResponse(News news);

    List<NewsResponse> toResponses(Page<News> news);

    @Mapping(target = "comments", source = "commentResponses")
    NewsWithCommentsResponse toWithCommentsResponse(News news, List<CommentResponse> commentResponses);

    News fromRequest(NewsMatcherRequest newsMatcherRequest);

}
