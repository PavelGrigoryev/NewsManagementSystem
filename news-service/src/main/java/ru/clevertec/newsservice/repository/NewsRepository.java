package ru.clevertec.newsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.clevertec.newsservice.model.News;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    @Query("""
            SELECT n FROM News n
            LEFT JOIN FETCH n.comments
            WHERE n.id = :id
            """)
    Optional<News> findByIdWithComments(Long id);

}
