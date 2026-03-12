package com.rewear.rewear.service;

import java.time.LocalDateTime;
import java.util.List;

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

  private void returnPointToUser(User user) {
    user.setPoints(user.getPoints() + 1);
    userRepository.save(user);
  }

  @Override
  public Reservation createReservation(Integer articleId, Integer userId) {

    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo no encontrado"));

    if (reservationRepository.existsByArticleId(articleId)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "El artículo ya está reservado");
    }

    if (article.getArticleStatus() != ArticleStatus.DISPONIBLE) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "El artículo no está disponible");
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    if (user.getPoints() <= 0) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Necesitas publicar un artículo para poder reservar");
    }

    user.setPoints(user.getPoints() - 1);
    userRepository.save(user);

    Reservation reservation = new Reservation();
    reservation.setArticle(article);
    reservation.setUser(user);

    article.setArticleStatus(ArticleStatus.RESERVADO);
    articleRepository.save(article);

    return reservationRepository.save(reservation);
  }

  @Override
  public void deleteReservation(Integer reservationId) {
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

    returnPointToUser(reservation.getUser());

    Article article = reservation.getArticle();
    article.setArticleStatus(ArticleStatus.DISPONIBLE);
    articleRepository.save(article);

    reservationRepository.delete(reservation);
  }

  @Override
  public void confirmExchange(Integer articleId) {
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artículo no encontrado"));

    if (article.getArticleStatus() != ArticleStatus.RESERVADO) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "El artículo no está en estado reservado");
    }

    article.setArticleStatus(ArticleStatus.INTERCAMBIADO);
    articleRepository.save(article);

    User owner = article.getUser();
    owner.setPoints(owner.getPoints() + 1);
    userRepository.save(owner);

    reservationRepository.findByArticleId(articleId)
        .ifPresent(r -> reservationRepository.delete(r));
  }

  @Override
  public void checkExpiredReservations() {
    List<Reservation> expired = reservationRepository
        .findByExpiresAtBefore(LocalDateTime.now());

    expired.forEach(reservation -> {
      returnPointToUser(reservation.getUser());

      Article article = reservation.getArticle();
      article.setArticleStatus(ArticleStatus.DISPONIBLE);
      articleRepository.save(article);

      reservationRepository.delete(reservation);
    });

  }
}
