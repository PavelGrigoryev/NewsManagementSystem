package ru.clevertec.newsservice.util.testbuilder.news;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.news.NewsUpdateRequest;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aNewsUpdateRequest")
@With
public class NewsUpdateRequestTestBuilder implements TestBuilder<NewsUpdateRequest> {

    private String title = "title";
    private String text = "text";

    @Override
    public NewsUpdateRequest build() {
        return new NewsUpdateRequest(title, text);
    }

}
