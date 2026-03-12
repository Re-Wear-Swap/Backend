package com.rewear.rewear.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, updatable = false)
  private LocalDateTime reservedAt;

  @Column(nullable = false, updatable = false)
  private LocalDateTime expiresAt;

  @OneToOne
  @JoinColumn(name = "article_id", unique = true, nullable = false)
  private Article article;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @PrePersist
  protected void onCreate() {
    this.reservedAt = LocalDateTime.now();
    this.expiresAt = LocalDateTime.now().plusHours(24);
  }

}
