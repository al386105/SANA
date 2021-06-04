package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.OccupancyFormData;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OccupancyFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return OccupancyFormData.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        OccupancyFormData occupancyFormData = (OccupancyFormData) obj;

        // Si busca por año, nos aseguramos que es valido
        if (occupancyFormData.getTypeOfPeriod().getDescripcion().equals("Por año") && occupancyFormData.getYear() <= 2019)
            errors.rejectValue("year", "incorrecto", "El año debe ser posterior a 2019");

        // Si busca por mes, nos aseguramos que es valido el año y el mes
        if (occupancyFormData.getTypeOfPeriod().getDescripcion().equals("Por mes")) {
            if(occupancyFormData.getYear() <= 2019)
                errors.rejectValue("year", "incorrecto", "El año debe ser posterior a 2019");
            if(occupancyFormData.getMonth().getNum() <= 0 || occupancyFormData.getMonth().getNum() > 12)
                errors.rejectValue("month", "incorrecto", "El mes es incorrecto");
        }

        // Si busca por dia, nos aseguramos que es valida la fecha
        if (occupancyFormData.getTypeOfPeriod().getDescripcion().equals("Por dia") && occupancyFormData.getDay() == null)
            errors.rejectValue("day", "obligatorio", "Es obligatorio introducir el día");
    }
}