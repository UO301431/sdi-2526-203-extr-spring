package com.uniovi.sdi.sdi2526extr203spring.validators;

import com.uniovi.sdi.sdi2526extr203spring.entities.OccupiedSlot;
import com.uniovi.sdi.sdi2526extr203spring.entities.Reservation;
import com.uniovi.sdi.sdi2526extr203spring.services.SpaceService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AddReservationValidator implements Validator {
    private final SpaceService spaceService;

    public AddReservationValidator(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Reservation.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Reservation reservation = (Reservation) target;

        //validar inicio anterior a final
        if(reservation.getStartDate().isAfter(reservation.getEndDate())){
            errors.rejectValue("startDate", "Error.date.startDateAfterEndDate");
        }

        //validar no reservas en el pasado
        LocalDateTime today = LocalDateTime.now();
        if(reservation.getStartDate().isBefore(today)){
            errors.rejectValue("startDate", "Error.date.startDateAfterToday");
        }

        //validar solapamientos
        List<OccupiedSlot> occupiedSlots = spaceService.getAvailabilityForSpace(
                reservation.getSpace().getId(),
                reservation.getStartDate(),
                reservation.getEndDate());
        if(!occupiedSlots.isEmpty()){
            errors.rejectValue("startDate", "Error.date.invalidate");
        }
    }
}
