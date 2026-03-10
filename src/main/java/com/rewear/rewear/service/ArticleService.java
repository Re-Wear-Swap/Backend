package com.rewear.rewear.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale.Category;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;

import com.rewear.rewear.entity.Article;

public interface ArticleService {

  public Article createArticle(Article article);

  public Page<Article> getArticles(Category category, LocalDateTime start, LocalDateTime end, Pageable pageable);

  public Article getArticleById(Integer userId);

  public List<Article> getArticlesByUserId(Integer userId);

  public Article updateArticle(Integer id, Article updatedArticle);

  public void deleteArticle(Integer id);

}
