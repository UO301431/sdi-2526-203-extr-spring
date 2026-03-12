package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import com.uniovi.sdi.sdi2526entrega121.entities.ReservationStatus;
import com.uniovi.sdi.sdi2526entrega121.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationsService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Page<Reservation> getReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    public Page<Reservation> getReservationsByUser(String dni, Pageable pageable) {
        return reservationRepository.findByUserDni(dni, pageable);
    }

    public Page<Reservation> getReservationsByUserAndStatus(String dni, ReservationStatus status, Pageable pageable) {
        return reservationRepository.findByUserDniAndStatus(dni, status, pageable);
    }

    public Page<Reservation> getFilteredReservations(Long spaceId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return reservationRepository.findByFilters(spaceId, startDate, endDate, pageable);
    }

    public void addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }
}
