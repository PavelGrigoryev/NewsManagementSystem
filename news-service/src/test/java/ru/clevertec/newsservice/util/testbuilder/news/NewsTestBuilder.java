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
    private String title = "В Беларуси прошли массовые акции протеста против алкоголизма";
    private String text = "В Беларуси прошли массовые акции протеста против алкоголизма, который удерживает власть над алкоголиками более 40 лет.";

    @Override
    public News build() {
        return News.builder()
                .id(id)
                .time(time)
                .title(title)
                .text(text)
                .build();
    }

}
