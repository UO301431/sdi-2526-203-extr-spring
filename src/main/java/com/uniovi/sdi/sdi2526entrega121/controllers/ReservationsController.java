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
    public String getReservations(Model model, Pageable pageable, Principal principal,
                                     @RequestParam(value = "spaceId", required = false) Long spaceId,
                                     @RequestParam(value = "status", required = false) ReservationStatus status,
                                     @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String dni = principal.getName();

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        Page<Reservation> reservations = reservationsService.getFilteredReservations(dni, status, spaceId, start, end, pageable);

        model.addAttribute("reservations", reservations.getContent());
        model.addAttribute("page", reservations);
        model.addAttribute("spaces", spaceService.getSpaces(pageable));
        model.addAttribute("statuses", ReservationStatus.values());

        return "reservation/list";
    }

    @RequestMapping(value = "/reservations/update")
    public String updateReservations(Model model, Pageable pageable, Principal principal,
                                     @RequestParam(value = "spaceId", required = false) Long spaceId,
                                     @RequestParam(value = "status", required = false) ReservationStatus status,
                                     @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String dni = principal.getName();

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        Page<Reservation> reservations = reservationsService.getFilteredReservations(dni, status, spaceId, start, end, pageable);

        model.addAttribute("reservations", reservations.getContent());
        model.addAttribute("page", reservations);

        return "reservation/list :: tableReservation";
    }
}