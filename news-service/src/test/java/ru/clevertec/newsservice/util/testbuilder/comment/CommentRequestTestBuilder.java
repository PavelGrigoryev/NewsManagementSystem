package ru.clevertec.newsservice.util.testbuilder.comment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.comment.CommentRequest;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentRequest")
@With
public class CommentRequestTestBuilder implements TestBuilder<CommentRequest> {

    private String text = "Ужас какой, что творится...";
    private String username = "Евлампия";

    @Override
    public CommentRequest build() {
        return new CommentRequest(text, username);
    }

}
