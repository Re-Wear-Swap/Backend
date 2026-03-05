package com.rewear.rewear.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rewear.rewear.entity.Article;
import com.rewear.rewear.entity.Reservation;
import com.rewear.rewear.entity.User;
import com.rewear.rewear.entity.enums.ArticleStatus;
import com.rewear.rewear.repository.ArticleRepository;
import com.rewear.rewear.repository.ReservationRepository;
import com.rewear.rewear.repository.UserRepository;

@Service
public class ReservationServiceImpl implements ReservationService {

  private final ReservationRepository reservationRepository;
  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;

  public ReservationServiceImpl(
      ReservationRepository reservationRepository,
      ArticleRepository articleRepository,
      UserRepository userRepository) {
    this.reservationRepository = reservationRepository;
    this.articleRepository = articleRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Reservation createReservation(Integer articleId, Integer userId) {

    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo no encontrado"));

    if (reservationRepository.existsByArticleId(articleId)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "El artículo ya está reservado");
    }

    if (article.getArcticleStatus() != ArticleStatus.DISPONIBLE) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "El artículo no está disponible");
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    Reservation reservation = new Reservation();
    reservation.setArticle(article);
    reservation.setUser(user);

    article.setArcticleStatus(ArticleStatus.RESERVADO);
    articleRepository.save(article);

    return reservationRepository.save(reservation);
  }

  @Override
  public void deleteReservation(Integer reservationId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

    Article article = reservation.getArticle();
    article.setArcticleStatus(ArticleStatus.DISPONIBLE);
    articleRepository.save(article);

    reservationRepository.delete(reservation);
  }
}
