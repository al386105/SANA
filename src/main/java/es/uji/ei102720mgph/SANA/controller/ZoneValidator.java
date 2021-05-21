package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.Zone;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ZoneValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return Zone.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Zone zone = (Zone)obj;

        // Letra obligatoria
        if (zone.getLetter().trim().equals(""))
            errors.rejectValue("letter", "obligatorio", "Es obligatorio completar la letra");

        // Una sola letra
        if (zone.getLetter().trim().length() != 1)
            errors.rejectValue("letter", "incorrecto", "Es obligatorio introducir una sola letra");

        // Número de zona mayor que 0
        if (zone.getZoneNumber() < 1)
            errors.rejectValue("zoneNumber", "incorrecto", "El número de zona debe ser superior a 0");
    }
}
