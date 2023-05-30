package ru.clevertec.newsservice.util.testbuilder.news;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.comment.CommentResponse;
import ru.clevertec.newsservice.dto.news.NewsWithCommentsResponse;
import ru.clevertec.newsservice.util.TestBuilder;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentResponseTestBuilder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aNewsWithCommentsResponse")
@With
public class NewsWithCommentsResponseTestBuilder implements TestBuilder<NewsWithCommentsResponse> {

    private Long id = 1L;
    private LocalDateTime time = LocalDateTime.of(2023, Month.MAY, 25, 15, 25, 33);
    private String title = "В Беларуси прошли массовые акции протеста против алкоголизма";
    private String text = "В Беларуси прошли массовые акции протеста против алкоголизма, который удерживает власть над алкоголиками более 40 лет.";
    private List<CommentResponse> comments = List.of(CommentResponseTestBuilder.aCommentResponse().build(),
            CommentResponseTestBuilder.aCommentResponse().withId(2L).withUsername("Саня").build());

    @Override
    public NewsWithCommentsResponse build() {
        return new NewsWithCommentsResponse(id, time, title, text, comments);
    }

}
