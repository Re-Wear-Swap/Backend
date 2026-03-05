package com.rewear.rewear.service;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import com.rewear.rewear.entity.Article;
import com.rewear.rewear.entity.enums.Category;

public interface ArticleService {

  Article createArticle(Article article);

  Page<Article> getArticles(int page);

  Page<Article> getArticlesByCategory(Category category, int page);

  Page<Article> getArticlesByDate(LocalDate start, LocalDate end, int page);

  Page<Article> getArticlesByCategoryAndDate(Category category, LocalDate start, LocalDate end, int page);

  Article getArticleById(Integer id);

  Article updateArticle(Integer id, Article article);

  void deleteArticle(Integer id);
}
