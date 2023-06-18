package ru.clevertec.newsservice.model.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsservice.model.Comment;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentTestBuilder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CommentListenerTest {

    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = CommentTestBuilder.aComment().build();
    }

    @Test
    @DisplayName("test prePersist should insert LocalDateTime")
    void testPrePersistShouldInsertLocalDateTime() {
        CommentListener commentListener = new CommentListener();

        commentListener.prePersist(comment);

        assertAll(
                () -> assertThat(LocalDateTime.now().getYear()).isEqualTo(comment.getTime().getYear()),
                () -> assertThat(LocalDateTime.now().getMonthValue()).isEqualTo(comment.getTime().getMonthValue()),
                () -> assertThat(LocalDateTime.now().getDayOfMonth()).isEqualTo(comment.getTime().getDayOfMonth()),
                () -> assertThat(LocalDateTime.now().getHour()).isEqualTo(comment.getTime().getHour()),
                () -> assertThat(LocalDateTime.now().getMinute()).isEqualTo(comment.getTime().getMinute())
        );
    }

    @Test
    @DisplayName("test preUpdate should update LocalDateTime")
    void testPreUpdateShouldUpdateLocalDateTime() {
        CommentListener commentListener = new CommentListener();

        commentListener.preUpdate(comment);

        assertAll(
                () -> assertThat(LocalDateTime.now().getYear()).isEqualTo(comment.getTime().getYear()),
                () -> assertThat(LocalDateTime.now().getMonthValue()).isEqualTo(comment.getTime().getMonthValue()),
                () -> assertThat(LocalDateTime.now().getDayOfMonth()).isEqualTo(comment.getTime().getDayOfMonth()),
                () -> assertThat(LocalDateTime.now().getHour()).isEqualTo(comment.getTime().getHour()),
                () -> assertThat(LocalDateTime.now().getMinute()).isEqualTo(comment.getTime().getMinute())
        );
    }

}
