package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.NuevaReserva;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ReservationValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return NuevaReserva.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        NuevaReserva res = (NuevaReserva) obj;

        // Cuerpo del comentario obligatorio
        if (res.getZoneid() == null)
            errors.rejectValue("zoneid", "obligatorio", "Seleccione una de las zonas");
    }
}
