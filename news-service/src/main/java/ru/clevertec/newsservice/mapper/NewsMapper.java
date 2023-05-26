package ru.clevertec.newsservice.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.newsservice.dto.NewsResponse;
import ru.clevertec.newsservice.dto.NewsWithCommentsResponse;
import ru.clevertec.newsservice.model.News;

@Mapper(uses = CommentMapper.class)
public interface NewsMapper {

    NewsResponse toResponse(News news);

    NewsWithCommentsResponse toWithCommentsResponse(News news);

}
