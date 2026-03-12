package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import com.uniovi.sdi.sdi2526entrega121.entities.ReservationStatus;
import com.uniovi.sdi.sdi2526entrega121.entities.User;
import com.uniovi.sdi.sdi2526entrega121.services.ReservationsService;
import com.uniovi.sdi.sdi2526entrega121.services.SpaceService;
import com.uniovi.sdi.sdi2526entrega121.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;

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

    private final UsersService usersService;

    public ReservationsController(UsersService usersService) {

        this.usersService = usersService;
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

    @RequestMapping(value = "/reservations/cancel/{id}")
    public String cancelReservation(@PathVariable Long id, Principal principal) {
        String dni = principal.getName();

        reservationsService.cancelReservation(id, dni);

        return "redirect:/reservations/list";
    }

    /**
     * Get de la vista de añadir reserva
     */
    @GetMapping("/reservations/add")
    public String getReservation(Model model){
        model.addAttribute("activeSpaces", spaceService.getActiveSpaces());
        model.addAttribute("reservation", new Reservation());
        return "reservation/add";
    }

    /**
     * Post para añadir la reserva recibida como parametro
     * @param reservation, reserva a añadir
     */
    @PostMapping("/reservations/add")
    public String setReservation(@ModelAttribute Reservation reservation,
                                 Principal principal){
        String dni = principal.getName();
        User user = usersService.getUserByDni(dni);
        reservation.setUser(user);
        reservationsService.addReservation(reservation);
        return "reservation/add";
    }
}