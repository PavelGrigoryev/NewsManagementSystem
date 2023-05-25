package ru.clevertec.newsservice.model.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import ru.clevertec.newsservice.model.News;

import java.time.LocalDateTime;

public class NewsListener {

    @PrePersist
    public void prePersist(News news) {
        news.setTime(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate(News news) {
        news.setTime(LocalDateTime.now());
    }

}
