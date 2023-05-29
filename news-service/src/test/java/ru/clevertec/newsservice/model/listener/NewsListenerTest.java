package ru.clevertec.newsservice.model.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsservice.model.News;
import ru.clevertec.newsservice.util.testbuilder.news.NewsTestBuilder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class NewsListenerTest {

    private News news;

    @BeforeEach
    void setUp() {
        news = NewsTestBuilder.aNews().build();
    }

    @Test
    @DisplayName("test prePersist should insert LocalDateTime")
    void testPrePersistShouldInsertLocalDateTime() {
        NewsListener newsListener = new NewsListener();

        newsListener.prePersist(news);

        assertAll(
                () -> assertThat(LocalDateTime.now().getYear()).isEqualTo(news.getTime().getYear()),
                () -> assertThat(LocalDateTime.now().getMonthValue()).isEqualTo(news.getTime().getMonthValue()),
                () -> assertThat(LocalDateTime.now().getDayOfMonth()).isEqualTo(news.getTime().getDayOfMonth()),
                () -> assertThat(LocalDateTime.now().getHour()).isEqualTo(news.getTime().getHour()),
                () -> assertThat(LocalDateTime.now().getMinute()).isEqualTo(news.getTime().getMinute())
        );
    }

    @Test
    @DisplayName("test preUpdate should update LocalDateTime")
    void testPreUpdateShouldUpdateLocalDateTime() {
        NewsListener newsListener = new NewsListener();

        newsListener.preUpdate(news);

        assertAll(
                () -> assertThat(LocalDateTime.now().getYear()).isEqualTo(news.getTime().getYear()),
                () -> assertThat(LocalDateTime.now().getMonthValue()).isEqualTo(news.getTime().getMonthValue()),
                () -> assertThat(LocalDateTime.now().getDayOfMonth()).isEqualTo(news.getTime().getDayOfMonth()),
                () -> assertThat(LocalDateTime.now().getHour()).isEqualTo(news.getTime().getHour()),
                () -> assertThat(LocalDateTime.now().getMinute()).isEqualTo(news.getTime().getMinute())
        );
    }

}
