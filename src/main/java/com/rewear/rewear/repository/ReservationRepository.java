package com.rewear.rewear.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rewear.rewear.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

  boolean existsByArticleId(Integer articleId);

  Optional<Reservation> findByArticleId(Integer articleId);

  List<Reservation> findByExpiresAtBefore(LocalDateTime dateTime);
}
