package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.model.Municipality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import es.uji.ei102720mgph.SANA.model.NaturalArea;

import java.util.List;
import java.util.stream.Collectors;

public class NaturalAreaValidator implements Validator {
    private MunicipalityDao municipalityDao;

    @Autowired
    public void setMunicipalityDao(MunicipalityDao municipalityDao) { this.municipalityDao = municipalityDao; }

    @Override
    public boolean supports(Class<?> cls) {
        return NaturalArea.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        NaturalArea naturalArea = (NaturalArea)obj;
        // Selecccionar municipio
        /*
        List<Municipality> municipalityList = municipalityDao.getMunicipalities();
        List<String> namesMunicipalities = municipalityList.stream()          // sols els seus noms
                .map(Municipality::getName)
                .collect(Collectors.toList());

        if (!namesMunicipalities.contains(naturalArea.getMunicipality())) {
            errors.rejectValue("municipality", "valor incorrecto",
                    "No se ha seleccionado un municipio");
        }*/
    }
}

