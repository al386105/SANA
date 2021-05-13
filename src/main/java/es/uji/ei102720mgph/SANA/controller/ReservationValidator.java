package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.RegistrationCitizen;
import es.uji.ei102720mgph.SANA.model.Reservation;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ReservationValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return Reservation.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Reservation reservation = (Reservation) obj;

        // TODO: Falta comprobar que la reserva no supera el limite de personas
        if (reservation.getNumberOfPeople() <= 0 )
            errors.rejectValue("numberOfPeople", "obligatorio", "Es obligatorio introducir un número de personas");

    }
}