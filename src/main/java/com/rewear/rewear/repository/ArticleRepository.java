package com.rewear.rewear.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rewear.rewear.entity.Article;
import com.rewear.rewear.entity.enums.Category;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Page<Article> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Article> findByCategoryOrderByCreatedAtDesc(Category category, Pageable pageable);

    Page<Article> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Article> findByCategoryAndCreatedAtBetweenOrderByCreatedAtDesc(
            Category category,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable);
}
