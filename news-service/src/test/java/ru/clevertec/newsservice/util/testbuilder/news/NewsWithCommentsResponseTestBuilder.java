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
    private String title = "Apple unveils iPhone 15 with holographic display";
    private String text = "Apple has announced its latest flagship smartphone, the iPhone 15, which features a revolutionary holographic display that projects 3D images in mid-air. The iPhone 15 also boasts a faster processor, a longer battery life, and a new design that is thinner and lighter than ever. The iPhone 15 will be available in stores starting from July 1st.";
    private List<CommentResponse> comments = List.of(CommentResponseTestBuilder.aCommentResponse().build(),
            CommentResponseTestBuilder.aCommentResponse().withId(2L).withUsername("Sasha").build());
    private String email = "tech@news.com";

    @Override
    public NewsWithCommentsResponse build() {
        return new NewsWithCommentsResponse(id, time, title, text, email, comments);
    }

}
