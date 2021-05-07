package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.Municipality;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MunicipalityValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return Municipality.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Municipality municipality = (Municipality)obj;

        // Nombre obligatorio
        if (municipality.getName().trim().equals(""))
            errors.rejectValue("name", "obligatorio", "Es obligatorio introducir el nombre del municipio");

        // Descripción obligatoria
        if (municipality.getDescription().trim().equals(""))
            errors.rejectValue("description", "obligatorio", "Es obligatorio introducir una descripción");
    }
}