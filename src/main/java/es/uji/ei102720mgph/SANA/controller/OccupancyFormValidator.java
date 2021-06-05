package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.OccupancyFormData;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class OccupancyFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return OccupancyFormData.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        OccupancyFormData occupancyFormData = (OccupancyFormData) obj;
        int anyoActual = LocalDate.now().getYear();

        // Si busca por año, nos aseguramos que es valido (entre 2020 y el anyo actual)
        if (occupancyFormData.getTypeOfPeriod().getDescripcion().equals("Por año"))
            if (occupancyFormData.getYear() <= 2019 || occupancyFormData.getYear() > anyoActual)
                errors.rejectValue("year", "incorrecto", "El año debe ser posterior a 2019 y anterior a " + anyoActual);

        // Si busca por mes, nos aseguramos que es valido el año y el mes
        if (occupancyFormData.getTypeOfPeriod().getDescripcion().equals("Por mes")) {
            if(occupancyFormData.getYear() <= 2019 || occupancyFormData.getYear() > anyoActual)
                errors.rejectValue("year", "incorrecto", "El año debe ser posterior a 2019 y anterior a " + anyoActual);
            if(occupancyFormData.getMonth().getNum() <= 0 || occupancyFormData.getMonth().getNum() > 12)
                errors.rejectValue("month", "incorrecto", "El mes es incorrecto");
            if(occupancyFormData.getYear() == anyoActual && occupancyFormData.getMonth().getNum() > LocalDate.now().getMonthValue())
                errors.rejectValue("month", "incorrecto", "No se puede consultar la ocupación de un mes posterior al actual");
        }

        // Si busca por dia, nos aseguramos que es valida la fecha
        if (occupancyFormData.getTypeOfPeriod().getDescripcion().equals("Por día")) {
            if (occupancyFormData.getDay() == null)
                errors.rejectValue("day", "obligatorio", "Es obligatorio introducir un día");
            else if (occupancyFormData.getDay().getYear() <= 2019)
                errors.rejectValue("day", "incorrecto", "El año debe ser posterior a 2019");
            else if (occupancyFormData.getDay().getYear() > anyoActual ||
                    occupancyFormData.getDay().getYear() == anyoActual && occupancyFormData.getDay().getMonth().getValue() > LocalDate.now().getMonthValue() ||
                    ( occupancyFormData.getDay().getYear() == anyoActual && occupancyFormData.getDay().getMonth().getValue() == LocalDate.now().getMonthValue()
                            && occupancyFormData.getDay().getDayOfMonth() > LocalDate.now().getDayOfMonth()))
                errors.rejectValue("day", "incorrecto", "No se puede consultar la ocupación de un día posterior al actual");
        }
    }
}