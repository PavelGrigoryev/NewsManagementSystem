package ru.clevertec.newsservice.util.testbuilder.comment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.proto.CommentRequest;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentRequest")
@With
public class CommentRequestTestBuilder implements TestBuilder<CommentRequest> {

    private String text = "It's terrible what's going on...";
    private String username = "Evlampia";

    @Override
    public CommentRequest build() {
        return CommentRequest.newBuilder()
                .setText(text)
                .setUsername(username)
                .build();
    }

}
