package ru.clevertec.newsservice.util.testbuilder.comment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.comment.CommentResponse;
import ru.clevertec.newsservice.util.TestBuilder;

import java.time.LocalDateTime;
import java.time.Month;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentResponse")
@With
public class CommentResponseTestBuilder implements TestBuilder<CommentResponse> {

    private Long id = 1L;
    private LocalDateTime time = LocalDateTime.of(2023, Month.JUNE, 3, 19, 44, 11);
    private String text = "Не очень понятно, надо бы больше примеров";
    private String username = "Ольга";

    @Override
    public CommentResponse build() {
        return new CommentResponse(id, time, text, username);
    }

}
