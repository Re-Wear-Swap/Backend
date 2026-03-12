package com.rewear.rewear.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.rewear.rewear.entity.Reservation;
import com.rewear.rewear.service.ReservationService;
import com.rewear.rewear.repository.ReservationRepository;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RequestMapping("/api/reservations")
public class ReservationController {

  private final ReservationService reservationService;
  private final ReservationRepository reservationRepository;

  public ReservationController(ReservationService reservationService, ReservationRepository reservationRepository) {
    this.reservationService = reservationService;
    this.reservationRepository = reservationRepository;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Reservation createReservation(@RequestParam Integer articleId, @RequestParam Integer userId) {
    return reservationService.createReservation(articleId, userId);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteReservation(@PathVariable Integer id) {
    reservationService.deleteReservation(id);
  }

  @DeleteMapping("/article/{articleId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancelByArticle(@PathVariable Integer articleId) {
    reservationRepository.findByArticleId(articleId)
        .ifPresent(r -> reservationService.deleteReservation(r.getId()));
  }

  @PutMapping("/{articleId}/confirm")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void confirmExchange(@PathVariable Integer articleId) {
    reservationService.confirmExchange(articleId);
  }

  @GetMapping("/user/{userId}")
  public List<Reservation> getReservationsByUser(@PathVariable Integer userId) {
    return reservationRepository.findByUserId(userId);
  }
}
