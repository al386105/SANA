package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.NaturalAreaForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class NaturalAreaValidadorRestricted implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return NaturalAreaForm.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        NaturalAreaForm naturalArea = (NaturalAreaForm) obj;

        // Si es restringida, es obligatorio la fecha de restricción
        if (naturalArea.getRestrictionTimePeriod() == null)
            errors.rejectValue("restrictionTimePeriod", "obligatorio", "Es obligatorio introducir la fecha de restricción");

        // La fecha de restricción debe ser posterior a la fecha actual
        if (naturalArea.getRestrictionTimePeriod() != null && naturalArea.getRestrictionTimePeriod().isBefore(LocalDate.now()))
            errors.rejectValue("restrictionTimePeriod", "incorrecto", "La fecha de restricción debe ser posterior a hoy");

    }
}