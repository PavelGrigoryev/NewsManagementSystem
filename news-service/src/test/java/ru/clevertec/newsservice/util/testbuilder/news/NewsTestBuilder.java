package ru.clevertec.newsservice.util.testbuilder.news;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.model.News;
import ru.clevertec.newsservice.util.TestBuilder;

import java.time.LocalDateTime;
import java.time.Month;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aNews")
@With
public class NewsTestBuilder implements TestBuilder<News> {

    private Long id = 1L;
    private LocalDateTime time = LocalDateTime.of(2023, Month.MAY, 25, 15, 25, 33);
    private String title = "Apple unveils iPhone 15 with holographic display";
    private String text = "Apple has announced its latest flagship smartphone, the iPhone 15, which features a revolutionary holographic display that projects 3D images in mid-air. The iPhone 15 also boasts a faster processor, a longer battery life, and a new design that is thinner and lighter than ever. The iPhone 15 will be available in stores starting from July 1st.";
    private String email = "tech@news.com";

    @Override
    public News build() {
        return News.builder()
                .id(id)
                .time(time)
                .title(title)
                .text(text)
                .email(email)
                .build();
    }

}
