package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.NaturalAreaForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
    }
}