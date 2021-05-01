package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.Zone;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import es.uji.ei102720mgph.SANA.model.NaturalArea;

public class ZoneValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return NaturalArea.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Zone zone = (Zone)obj;

        //TODO
    }
}
