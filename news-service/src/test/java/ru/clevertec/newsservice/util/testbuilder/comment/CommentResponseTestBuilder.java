package ru.clevertec.newsservice.util.testbuilder.comment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.proto.CommentResponse;
import ru.clevertec.newsservice.util.TestBuilder;

import java.time.LocalDateTime;
import java.time.Month;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aCommentResponse")
@With
public class CommentResponseTestBuilder implements TestBuilder<CommentResponse> {

    private Long id = 1L;
    private LocalDateTime time = LocalDateTime.of(2023, Month.JUNE, 14, 10, 31, 12);
    private String text = "Wow! That's scary! I hope everyone is safe!";
    private String username = "LavaLover";
    private String email = "lavalover@gmail.com";

    @Override
    public CommentResponse build() {
        return CommentResponse.newBuilder()
                .setId(id)
                .setTime(time.toString())
                .setText(text)
                .setUsername(username)
                .setEmail(email)
                .build();
    }

}
