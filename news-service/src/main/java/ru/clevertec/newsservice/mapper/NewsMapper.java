package ru.clevertec.newsservice.mapper;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import ru.clevertec.newsservice.dto.news.NewsRequest;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.newsservice.model.News;

import java.util.List;

@Mapper
public interface NewsMapper {

    NewsResponse toResponse(News news);

    List<NewsResponse> toResponses(Page<News> news);

    News fromRequest(NewsRequest newsRequest);

    News fromResponse(NewsResponse newsResponse);

}
