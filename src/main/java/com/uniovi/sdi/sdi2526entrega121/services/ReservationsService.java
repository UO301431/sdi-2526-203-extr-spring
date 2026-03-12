package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import com.uniovi.sdi.sdi2526entrega121.entities.ReservationStatus;
import com.uniovi.sdi.sdi2526entrega121.entities.User;
import com.uniovi.sdi.sdi2526entrega121.repositories.ReservationRepository;
import com.uniovi.sdi.sdi2526entrega121.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReservationsService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UsersRepository usersRepository;

    public Page<Reservation> getReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    public Page<Reservation> getFilteredReservations(String dni, ReservationStatus status, Long spaceId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        User user = usersRepository.findByDni(dni);
        String userRole = user.getRole();

        if(userRole.equals("ROLE_ADMIN")) {
            return reservationRepository.findByFilters(status, spaceId, startDate, endDate, pageable);
        } else if (userRole.equals("ROLE_EMPLOYEE")) {
            return reservationRepository.findByUserAndFilters(user.getId(), status, spaceId, startDate, endDate, pageable);
        }
        return Page.empty();
    }

    public void cancelReservation(Long reservationId, String dni) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation != null) {
            if (reservation.getUser().getDni().equals(dni)) {
                reservation.setStatus(ReservationStatus.CANCELLED);

                reservationRepository.save(reservation);
            }
        }
    }
}
