package ru.clevertec.newsservice.util.testbuilder.news;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.proto.NewsResponse;
import ru.clevertec.newsservice.util.TestBuilder;

import java.time.LocalDateTime;
import java.time.Month;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aNewsResponse")
@With
public class NewsResponseTestBuilder implements TestBuilder<NewsResponse> {

    private Long id = 1L;
    private LocalDateTime time = LocalDateTime.of(2023, Month.JUNE, 14, 10, 40, 15);
    private String title = "Apple unveils iPhone 15 with holographic display";
    private String text = "Apple has announced its latest flagship smartphone, the iPhone 15, which features" +
                          " a revolutionary holographic display that projects 3D images in mid-air. The iPhone 15 " +
                          "also boasts a faster processor, a longer battery life, and a new design that is thinner" +
                          " and lighter than ever. The iPhone 15 will be available in stores starting from July 1st.";
    private String email = "tech@news.com";

    @Override
    public NewsResponse build() {
        return NewsResponse.newBuilder()
                .setId(id)
                .setTime(time.toString())
                .setTitle(title)
                .setText(text)
                .setEmail(email)
                .build();
    }

}
