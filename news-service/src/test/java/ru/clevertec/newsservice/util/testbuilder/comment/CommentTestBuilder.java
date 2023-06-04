package ru.clevertec.newsservice.util.testbuilder.comment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.model.Comment;
import ru.clevertec.newsservice.util.TestBuilder;

import java.time.LocalDateTime;
import java.time.Month;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aComment")
@With
public class CommentTestBuilder implements TestBuilder<Comment> {

    private Long id = 1L;
    private LocalDateTime time = LocalDateTime.of(2023, Month.JUNE, 3, 19, 44, 11);
    private String text = "Не очень понятно, надо бы больше примеров";
    private String username = "Ольга";
    private String email = "olga1989@yandex.ru";

    @Override
    public Comment build() {
        return Comment.builder()
                .id(id)
                .time(time)
                .text(text)
                .username(username)
                .email(email)
                .build();
    }

}
