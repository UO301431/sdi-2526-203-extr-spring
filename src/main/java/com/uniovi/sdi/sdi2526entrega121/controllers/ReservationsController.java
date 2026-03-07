package com.uniovi.sdi.sdi2526entrega121.controllers;

import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import com.uniovi.sdi.sdi2526entrega121.services.ReservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReservationsController {

    @Autowired
    private ReservationsService reservationsService;

    public ReservationsController() {

    }

    @RequestMapping(value = "/reservations/list")
    public String getList(Model model, Pageable pageable) {
        Page<Reservation> departments = reservationsService.getReservations(pageable);

        model.addAttribute("reservationList", departments.getContent());
        model.addAttribute("page", departments);

        return "reservation/list";
    }

}
