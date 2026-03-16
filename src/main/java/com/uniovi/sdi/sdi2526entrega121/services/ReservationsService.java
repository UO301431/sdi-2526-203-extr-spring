package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import com.uniovi.sdi.sdi2526entrega121.entities.ReservationStatus;
import com.uniovi.sdi.sdi2526entrega121.entities.User;
import com.uniovi.sdi.sdi2526entrega121.entities.RecurrenceFrequency;
import com.uniovi.sdi.sdi2526entrega121.repositories.ReservationRepository;
import com.uniovi.sdi.sdi2526entrega121.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public void addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public boolean createRecurringReservations(Reservation base,
                                               RecurrenceFrequency frequency,
                                               LocalDate endDate) {
        List<Reservation> toSave = new ArrayList<>();

        LocalDateTime currentStart = base.getStartDate();
        LocalDateTime currentEnd = base.getEndDate();
        long durationMinutes = java.time.Duration.between(currentStart, currentEnd).toMinutes();

        while (!currentStart.toLocalDate().isAfter(endDate)) {
            boolean overlapReservation = reservationRepository.existsActiveOverlap(
                    base.getSpace().getId(),
                    currentStart,
                    currentEnd,
                    ReservationStatus.ACTIVE
            );
            boolean overlapBlock = reservationRepository.existsActiveBlockOverlap(
                    base.getSpace().getId(),
                    currentStart,
                    currentEnd
            );

            if (overlapReservation || overlapBlock) {
                return false;
            }

            Reservation r = new Reservation(
                    base.getUser(),
                    base.getSpace(),
                    currentStart,
                    currentEnd,
                    base.getReason()
            );
            r.setIsRecurring(true);
            r.setRecurrenceFrequency(frequency);
            r.setRecurrenceEndDate(endDate);
            toSave.add(r);

            switch (frequency) {
                case DAILY:
                    currentStart = currentStart.plusDays(1);
                    break;
                case WEEKLY:
                    currentStart = currentStart.plusWeeks(1);
                    break;
                case MONTHLY:
                    currentStart = currentStart.plusMonths(1);
                    break;
                case YEARLY:
                    currentStart = currentStart.plusYears(1);
                    break;
            }
            currentEnd = currentStart.plusMinutes(durationMinutes);
        }

        reservationRepository.saveAll(toSave);
        Long groupId = toSave.get(0).getId();
        for (Reservation r : toSave) {
            r.setRecurrenceGroupId(groupId);
        }
        reservationRepository.saveAll(toSave);

        return true;
    }
}
