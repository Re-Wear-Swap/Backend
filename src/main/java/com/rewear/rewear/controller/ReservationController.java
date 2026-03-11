package com.rewear.rewear.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.rewear.rewear.entity.Reservation;
import com.rewear.rewear.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

  private final ReservationService reservationService;

  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Reservation createReservation(
      @RequestParam Integer articleId,
      @RequestParam Integer userId) {
    return reservationService.createReservation(articleId, userId);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteReservation(@PathVariable Integer id) {
    reservationService.deleteReservation(id);
  }

  @PutMapping("/{articleId}/confirm")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void confirmExchange(@PathVariable Integer articleId) {
    reservationService.confirmExchange(articleId);
  }
}
