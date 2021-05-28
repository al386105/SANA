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

        // Area natural obligatorio
        if (occupancyFormData.getNaturalArea().trim().equals("") || occupancyFormData.getNaturalArea().equals("No seleccionado"))
            errors.rejectValue("naturalArea", "obligatorio", "Es obligatorio introducir el área natural");

        // TypeOfPeriod obligatorio
        if (occupancyFormData.getTypeOfPeriod() == null) {
            errors.rejectValue("typeOfPeriod", "obligatorio", "Es obligatorio introducir el tipo de periodo");
        }

        // Si busca por año, nos aseguramos que es valido
        if (occupancyFormData.getTypeOfPeriod().getDescripcion().equals("Por año") && occupancyFormData.getYear() <= 2010){
            errors.rejectValue("year", "obligatorio", "Es obligatorio introducir el año correcto para obtener el gráfico");
        }

        // Si busca por mes, nos aseguramos que es valido el año y el mes
        if (occupancyFormData.getTypeOfPeriod().getDescripcion().equals("Por mes") && occupancyFormData.getYear() <= 2010) {
            errors.rejectValue("year", "obligatorio", "Es obligatorio introducir el año correcto para obtener el gráfico");
            if (occupancyFormData.getMonth() <= 0 || occupancyFormData.getMonth() > 12) {
                errors.rejectValue("month", "obligatorio", "Es obligatorio introducir el mes correctamente para obtener el gráfico");
            }
        }

        // Si busca por dia, nos aseguramos que es valida la fecha
        if (occupancyFormData.getTypeOfPeriod().getDescripcion().equals("Por dia") && occupancyFormData.getDay() == null)
            errors.rejectValue("day", "obligatorio", "Es obligatorio introducir el día");
    }
}