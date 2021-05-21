package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.model.Municipality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Controller
public class MunicipalityValidator implements Validator {
    private MunicipalityDao municipalityDao;

    @Override
    public boolean supports(Class<?> cls) {
        return Municipality.class.equals(cls);
    }

    @Autowired
    public void setMunicipalityDao(MunicipalityDao municipalityDao) { this.municipalityDao = municipalityDao; }

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