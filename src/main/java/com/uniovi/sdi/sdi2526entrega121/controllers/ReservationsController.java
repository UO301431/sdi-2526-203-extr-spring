package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import com.uniovi.sdi.sdi2526entrega121.entities.ReservationStatus;
import com.uniovi.sdi.sdi2526entrega121.services.ReservationsService;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class ReservationsController {

    @Autowired
    private ReservationsService reservationsService;

    @Autowired
    private SpaceService spaceService;

    public ReservationsController() {

    }

    @RequestMapping(value = "/reservations/list")
    public String getOwnReservations(Model model,
                                     Pageable pageable,
                                     Principal principal,
                                     @RequestParam(value = "status", required = false) ReservationStatus status) {

        // String dni = principal.getName();
        String dni = "10000001S";

        Page<Reservation> reservations;

        if (status != null) {
            reservations = reservationsService.getReservationsByUserAndStatus(dni, status, pageable);
        } else {
            reservations = reservationsService.getReservationsByUser(dni, pageable);
        }

        model.addAttribute("reservationList", reservations.getContent());
        model.addAttribute("page", reservations);
        model.addAttribute("statuses", ReservationStatus.values());

        return "reservation/user-list";
    }

    @RequestMapping(value = "/admin/reservations/list")
    public String getAllReservations(Model model, Pageable pageable,
                                     @RequestParam(value = "spaceId", required = false) Long spaceId,
                                     @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Page<Reservation> reservations = getAdminReservationsPage(spaceId, startDate, endDate, pageable);

        model.addAttribute("reservations", reservations.getContent());
        model.addAttribute("page", reservations);
        model.addAttribute("spaces", spaceService.getSpaces(pageable));

        return "reservation/admin-list";
    }

    @RequestMapping(value = "/admin/reservations/update")
    public String updateAllReservations(Model model,
                                        Pageable pageable,
                                        @RequestParam(value = "spaceId", required = false) Long spaceId,
                                        @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                        @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Page<Reservation> reservations = getAdminReservationsPage(spaceId, startDate, endDate, pageable);

        model.addAttribute("reservations", reservations.getContent());
        model.addAttribute("page", reservations); // Añadido para que el paginador también se actualice con AJAX

        return "reservation/admin-list :: tableReservation";
    }

    private Page<Reservation> getAdminReservationsPage(Long spaceId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        if (spaceId == null && start == null && end == null) {
            return reservationsService.getReservations(pageable);
        } else {
            return reservationsService.getFilteredReservations(spaceId, start, end, pageable);
        }
    }
}