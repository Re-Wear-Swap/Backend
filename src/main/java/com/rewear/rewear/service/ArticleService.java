package com.rewear.rewear.service;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import com.rewear.rewear.entity.Article;
import com.rewear.rewear.entity.enums.Category;

public interface ArticleService {

  public Article createArticle(Article article);

  public Page<Article> getArticles(int page);

  public Page<Article> getArticlesByCategory(Category category, int page);

  public Page<Article> getArticlesByDate(LocalDate start, LocalDate end, int page);

  public Page<Article> getArticlesByCategoryAndDate(Category category, LocalDate start, LocalDate end, int page);

  public Article getArticleById(Integer id);

  public Article updateArticle(Integer id, Article article);

  public void deleteArticle(Integer id);
}
