package com.uniovi.sdi.sdi2526entrega121.validators;

import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class AddReservationValidator implements Validator {


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


    }
}
