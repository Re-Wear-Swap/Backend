package com.rewear.rewear.service;

import com.rewear.rewear.entity.Reservation;

public interface ReservationService {

  public Reservation createReservation(Integer articleId, Integer userId);

  public void deleteReservation(Integer reservationId);

  public void confirmExchange(Integer articleId);

  public void checkExpiredReservations();
}
