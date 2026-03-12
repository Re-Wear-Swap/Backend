package com.rewear.rewear.controller;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.rewear.rewear.entity.Article;
import com.rewear.rewear.entity.Reservation;
import com.rewear.rewear.entity.enums.ArticleStatus;
import com.rewear.rewear.entity.enums.Category;
import com.rewear.rewear.repository.ReservationRepository;
import com.rewear.rewear.service.ArticleService;
import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ArticleController {

    private final ArticleService articleService;
    private final ReservationRepository reservationRepository;

    public ArticleController(ArticleService articleService, ReservationRepository reservationRepository) {
        this.articleService = articleService;
        this.reservationRepository = reservationRepository;
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
            return articleService.getArticlesByCategoryAndDate(category, LocalDate.parse(startDate), LocalDate.parse(endDate), page);
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

    @GetMapping("/{id}/reservation")
    public Optional<Reservation> getReservationByArticle(@PathVariable Integer id) {
        return reservationRepository.findByArticleId(id);
    }

    @PutMapping("/{id}")
    public Article updateArticle(@PathVariable Integer id, @Valid @RequestBody Article article) {
        return articleService.updateArticle(id, article);
    }

    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticle(id);
    }

    @PatchMapping("/{id}/status")
    public Article updateStatus(@PathVariable Integer id, @RequestParam ArticleStatus status) {
        Article article = articleService.getArticleById(id);
        article.setArticleStatus(status);
        return articleService.updateArticle(id, article);
    }

    @GetMapping("/user/{userId}")
    public java.util.List<Article> getArticlesByUser(@PathVariable Integer userId) {
        return articleService.getArticles(0).getContent().stream()
            .filter(a -> a.getUser() != null && a.getUser().getId().equals(userId))
            .toList();
    }
}
