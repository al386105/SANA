package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.enums.Orientation;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.model.Municipality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import es.uji.ei102720mgph.SANA.model.NaturalArea;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        // Nombre obligatorio
        if (naturalArea.getName().trim().equals(""))
            errors.rejectValue("name", "obligatorio", "Es obligatorio completar el nombre");

        // Selecccionar acceso
        List<String> namesTypesOfAccess = Stream.of(TypeOfAccess.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        if (!namesTypesOfAccess.contains(naturalArea.getTypeOfAccess().name())) {
            errors.rejectValue("typeOfAccess", "obligatorio", "No se ha seleccionado un tipo de acceso");
        }

        // Coordenadoas obligatorias
        if (naturalArea.getGeographicalLocation().trim().equals(""))
            errors.rejectValue("geographicalLocation", "obligatorio", "Es obligatorio completar las coordenadas geográficas");

        // Largo obligatorio??????

        // Ancho obligatorio?????

        // Caracteristicas fisicas obligatorias
        if (naturalArea.getPhysicalCharacteristics().trim().equals(""))
            errors.rejectValue("physicalCharacteristics", "obligatorio", "Es obligatorio completar las características físicas");

        // Descripción obligatoria
        if (naturalArea.getDescription().trim().equals(""))
            errors.rejectValue("description", "obligatorio", "Es obligatorio completar la descripción");

        // Selecccionar orientacion
        List<String> namesOrientation = Stream.of(Orientation.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        if (!namesOrientation.contains(naturalArea.getOrientation().name())) {
            errors.rejectValue("orientation", "obligatorio", "No se ha seleccionado la orientación");
        }

        // Selecccionar municipio
        /*
        if(municipalityDao ==  null) System.out.println("ES NULL");
        List<Municipality> municipalityList = municipalityDao.getMunicipalities();
        List<String> namesMunicipalities = municipalityList.stream()
                .map(Municipality::getName)
                .collect(Collectors.toList());
        if (!namesMunicipalities.contains(naturalArea.getMunicipality())) {
            errors.rejectValue("municipality", "obligatorio", "No se ha seleccionado un municipio");
        }*/
    }
}

