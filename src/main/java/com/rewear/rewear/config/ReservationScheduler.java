package com.rewear.rewear.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rewear.rewear.service.ReservationService;

@Component
public class ReservationScheduler {

  private final ReservationService reservationService;

  public ReservationScheduler(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @Scheduled(fixedRate = 3600000)
  public void checkExpiredReservation() {
    reservationService.checkExpiredReservations();
  }

}
