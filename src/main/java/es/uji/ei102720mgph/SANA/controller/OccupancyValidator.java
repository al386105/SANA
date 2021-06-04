package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.OccupancyFormData;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OccupancyValidator implements Validator {

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
        if (occupancyFormData.getTypeOfPeriod() == null)
            errors.rejectValue("typeOfPeriod", "obligatorio", "Es obligatorio introducir el tipo de periodo");
    }
}