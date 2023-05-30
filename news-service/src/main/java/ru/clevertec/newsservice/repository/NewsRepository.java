package ru.clevertec.newsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.newsservice.model.News;

public interface NewsRepository extends JpaRepository<News, Long> {
}
