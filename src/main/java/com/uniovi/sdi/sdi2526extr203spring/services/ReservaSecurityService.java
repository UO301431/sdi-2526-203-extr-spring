package com.uniovi.sdi.sdi2526extr203spring.services;

import com.uniovi.sdi.sdi2526extr203spring.entities.Reservation;
import com.uniovi.sdi.sdi2526extr203spring.repositories.ReservationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReservaSecurityService {
    private final ReservationRepository resRepo;

    public ReservaSecurityService(ReservationRepository resRepo) {
        this.resRepo = resRepo;
    }

    public boolean isOwner(Authentication auth, Long idReserva) {
        // En caso de ser admin tiene permiso a todas
        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return true;
        }

        Optional<Reservation> reservation = resRepo.findById(idReserva);
        if(reservation.isEmpty()){
            return false;
        }

        String loggedInUser = auth.getName();
        return reservation.get().getUser().getDni().equals(loggedInUser);
    }
}
