package ru.clevertec.newsservice.util.testbuilder.news;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.proto.NewsRequest;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aNewsRequest")
@With
public class NewsRequestTestBuilder implements TestBuilder<NewsRequest> {

    private String title = "title";
    private String text = "text";

    @Override
    public NewsRequest build() {
        return NewsRequest.newBuilder()
                .setTitle(title)
                .setText(text)
                .build();
    }

}
