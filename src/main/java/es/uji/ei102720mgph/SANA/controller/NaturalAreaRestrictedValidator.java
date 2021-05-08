package es.uji.ei102720mgph.SANA.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import es.uji.ei102720mgph.SANA.model.NaturalArea;

public class NaturalAreaRestrictedValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return NaturalArea.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        NaturalArea naturalArea = (NaturalArea)obj;

        // Fecha de restriccion obligatoria
        if (naturalArea.getRestrictionTimePeriod() == null)
            errors.rejectValue("restrictionTimePeriod", "obligatorio", "Es obligatorio introducir la fecha " +
                    " de fin de restricción");
    }
}
