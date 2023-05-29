package ru.clevertec.newsservice.util.testbuilder.comment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.comment.CommentWithNewsRequest;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentWithNewsRequest")
@With
public class CommentWithNewsRequestTestBuilder implements TestBuilder<CommentWithNewsRequest> {

    private String text = "Не очень понятно, надо бы больше примеров";
    private String username = "Ольга";
    private Long newsId = 1L;

    @Override
    public CommentWithNewsRequest build() {
        return new CommentWithNewsRequest(text, username, newsId);
    }

}
