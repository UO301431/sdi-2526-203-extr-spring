package com.uniovi.sdi.sdi2526entrega121.services;

import com.uniovi.sdi.sdi2526entrega121.entities.*;
import com.uniovi.sdi.sdi2526entrega121.repositories.ReservationRepository;
import com.uniovi.sdi.sdi2526entrega121.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${reservation.max.active}")
    private int maxActiveReservations;

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

    public boolean hasReachedLimit(Long userId) {
        return reservationRepository.countActiveByUser(userId) >= maxActiveReservations;
    }

    public void addReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    public RecurringResult createRecurringReservations(Reservation base,
                                               RecurrenceFrequency frequency,
                                               LocalDate endDate) {
        List<Reservation> toSave = new ArrayList<>();

        LocalDateTime currentStart = base.getStartDate();
        LocalDateTime currentEnd = base.getEndDate();
        long durationMinutes = java.time.Duration.between(currentStart, currentEnd).toMinutes();

        long currentActive = reservationRepository.countActiveByUser(base.getUser().getId());
        long occurrences = 0;
        LocalDateTime tmpStart = currentStart;
        while (!tmpStart.toLocalDate().isAfter(endDate)) {
            occurrences++;
            tmpStart = switch (frequency) {
                case DAILY -> tmpStart.plusDays(1);
                case WEEKLY -> tmpStart.plusWeeks(1);
                case MONTHLY -> tmpStart.plusMonths(1);
                case YEARLY -> tmpStart.plusYears(1);
            };
        }
        if (currentActive + occurrences > maxActiveReservations) {
            return RecurringResult.LIMIT_REACHED;
        }

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
                return RecurringResult.OVERLAP;
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

            currentStart = switch (frequency) {
                case DAILY -> currentStart.plusDays(1);
                case WEEKLY -> currentStart.plusWeeks(1);
                case MONTHLY -> currentStart.plusMonths(1);
                case YEARLY -> currentStart.plusYears(1);
            };
            currentEnd = currentStart.plusMinutes(durationMinutes);
        }

        reservationRepository.saveAll(toSave);
        Long groupId = toSave.getFirst().getId();
        for (Reservation r : toSave) {
            r.setRecurrenceGroupId(groupId);
        }
        reservationRepository.saveAll(toSave);

        return RecurringResult.SUCCESS;
    }
}
