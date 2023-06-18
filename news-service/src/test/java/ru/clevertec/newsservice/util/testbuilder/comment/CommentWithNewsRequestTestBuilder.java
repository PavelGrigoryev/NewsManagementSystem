package ru.clevertec.newsservice.util.testbuilder.comment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.proto.CommentWithNewsRequest;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentWithNewsRequest")
@With
public class CommentWithNewsRequestTestBuilder implements TestBuilder<CommentWithNewsRequest> {

    private String text = "Wow! That's scary! I hope everyone is safe!";
    private String username = "LavaLover";
    private Long newsId = 1L;

    @Override
    public CommentWithNewsRequest build() {
        return CommentWithNewsRequest.newBuilder()
                .setText(text)
                .setUsername(username)
                .setNewsId(newsId)
                .build();
    }

}
