package com.rewear.rewear.service;

import com.rewear.rewear.entity.Reservation;

public interface ReservationService {
  Reservation createReservation(Integer articleId, Integer userId);
  void deleteReservation(Integer reservationId);
  void confirmExchange(Integer articleId);
  void checkExpiredReservations();
}
