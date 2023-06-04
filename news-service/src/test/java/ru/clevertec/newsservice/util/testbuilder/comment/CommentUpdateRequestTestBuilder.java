package ru.clevertec.newsservice.util.testbuilder.comment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.comment.CommentUpdateRequest;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentUpdateRequest")
@With
public class CommentUpdateRequestTestBuilder implements TestBuilder<CommentUpdateRequest> {

    private String text = "Ужас какой, что творится...";
    private String username = "Евлампия";

    @Override
    public CommentUpdateRequest build() {
        return new CommentUpdateRequest(text, username);
    }

}
