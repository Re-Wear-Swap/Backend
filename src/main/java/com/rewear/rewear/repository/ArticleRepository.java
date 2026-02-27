package com.rewear.rewear.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale.Category;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rewear.rewear.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

  public Page<Article> findAllByOrderByCreatedDateAtDesc(Pageable pageable);

  public Page<Article> findByCategoryOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end, Pageable pageable);

  public Page<Article> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end,
      Pageable pageable);

  public Page<Article> findByCategoryAndCreatedAtBetweenOrderByCreatedAtDesc(Category category, LocalDateTime start,
      LocalDateTime end, Pageable pageable);

  public List<Article> findByUserIdOrderByCreatedAtDesc(Integer userId);

}
