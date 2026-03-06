package com.rewear.rewear.entity;

import java.time.LocalDateTime;

import com.rewear.rewear.entity.enums.Category;
import com.rewear.rewear.entity.enums.ArticleStatus;
import com.rewear.rewear.entity.enums.ItemCondition;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 150)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Category category;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ArticleStatus articleStatus = ArticleStatus.DISPONIBLE;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ItemCondition itemCondition;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
