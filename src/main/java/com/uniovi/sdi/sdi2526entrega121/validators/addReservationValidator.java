package com.uniovi.sdi.sdi2526entrega121.validators;

import com.uniovi.sdi.sdi2526entrega121.entities.Reservation;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class addReservationValidator implements Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        return Reservation.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Reservation reservation = (Reservation) target;

        //validar inicio anterior a final
        //validar no reservas en el pasado
    }
}
