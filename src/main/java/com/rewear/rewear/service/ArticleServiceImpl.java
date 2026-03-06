package com.rewear.rewear.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rewear.rewear.entity.Article;
import com.rewear.rewear.entity.enums.Category;
import com.rewear.rewear.repository.ArticleRepository;

@Service
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private static final int PAGE_SIZE = 30;

  public ArticleServiceImpl(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  @Override
  public Article createArticle(Article article) {
    return articleRepository.save(article);
  }

  @Override
  public Page<Article> getArticles(int page) {
    return articleRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, PAGE_SIZE));
  }

  @Override
  public Page<Article> getArticlesByCategory(Category category, int page) {
    return articleRepository.findByCategoryOrderByCreatedAtDesc(category, PageRequest.of(page, PAGE_SIZE));
  }

  @Override
  public Page<Article> getArticlesByDate(LocalDate start, LocalDate end, int page) {
    LocalDateTime startDate = start.atStartOfDay();
    LocalDateTime endDate = end.plusDays(1).atStartOfDay().minusNanos(1);
    return articleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(
        startDate, endDate, PageRequest.of(page, PAGE_SIZE));
  }

  @Override
  public Page<Article> getArticlesByCategoryAndDate(Category category, LocalDate start, LocalDate end, int page) {
    LocalDateTime startDate = start.atStartOfDay();
    LocalDateTime endDate = end.plusDays(1).atStartOfDay().minusNanos(1);
    return articleRepository.findByCategoryAndCreatedAtBetweenOrderByCreatedAtDesc(
        category, startDate, endDate, PageRequest.of(page, PAGE_SIZE));
  }

  @Override
  public Article getArticleById(Integer id) {
    return articleRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo no encontrado"));
  }

  @Override
  public Article updateArticle(Integer id, Article updated) {
    Article existing = getArticleById(id);
    existing.setTitle(updated.getTitle());
    existing.setDescription(updated.getDescription());
    existing.setImageUrl(updated.getImageUrl());
    existing.setCategory(updated.getCategory());
    existing.setItemCondition(updated.getItemCondition());
    existing.setArticleStatus(updated.getArticleStatus());
    return articleRepository.save(existing);
  }

  @Override
  public void deleteArticle(Integer id) {
    if (!articleRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo no encontrado");
    }
    articleRepository.deleteById(id);
  }
}
