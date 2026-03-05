package com.rewear.rewear.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.rewear.rewear.entity.Article;
import com.rewear.rewear.entity.enums.Category;
import com.rewear.rewear.service.ArticleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

  private final ArticleService articleService;

  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @PostMapping
  public Article createArticle(@Valid @RequestBody Article article) {
    return articleService.createArticle(article);
  }

  @GetMapping
  public Page<Article> getArticles(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(required = false) Category category,
      @RequestParam(required = false) String startDate,
      @RequestParam(required = false) String endDate) {

    if (category != null && startDate != null && endDate != null) {
      return articleService.getArticlesByCategoryAndDate(
          category, LocalDate.parse(startDate), LocalDate.parse(endDate), page);
    } else if (category != null) {
      return articleService.getArticlesByCategory(category, page);
    } else if (startDate != null && endDate != null) {
      return articleService.getArticlesByDate(LocalDate.parse(startDate), LocalDate.parse(endDate), page);
    } else {
      return articleService.getArticles(page);
    }
  }

  @GetMapping("/{id}")
  public Article getArticleById(@PathVariable Integer id) {
    return articleService.getArticleById(id);
  }

  @PutMapping("/{id}")
  public Article updateArticle(@PathVariable Integer id, @Valid @RequestBody Article article) {
    return articleService.updateArticle(id, article);
  }

  @DeleteMapping("/{id}")
  public void deleteArticle(@PathVariable Integer id) {
    articleService.deleteArticle(id);
  }
}
