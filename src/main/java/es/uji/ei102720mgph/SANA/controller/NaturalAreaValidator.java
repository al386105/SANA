package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.dao.NaturalAreaDao;
import es.uji.ei102720mgph.SANA.enums.Orientation;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.enums.TypeOfArea;
import es.uji.ei102720mgph.SANA.model.Municipality;
import es.uji.ei102720mgph.SANA.model.NaturalAreaForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import es.uji.ei102720mgph.SANA.model.NaturalArea;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO no va el validador de selectores ni con @Component ni @Controller

public class NaturalAreaValidator implements Validator {
    private MunicipalityDao municipalityDao;
    private NaturalAreaDao naturalAreaDao;

    @Autowired
    public void setMunicipalityDao(MunicipalityDao municipalityDao) { this.municipalityDao = municipalityDao; }

    @Autowired
    public void setNaturalAreaDao(NaturalAreaDao naturalAreaDao) { this.naturalAreaDao = naturalAreaDao; }

    @Override
    public boolean supports(Class<?> cls) {
        return NaturalAreaForm.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        NaturalAreaForm naturalArea = (NaturalAreaForm)obj;

        // Nombre obligatorio
        if (naturalArea.getName().trim().equals(""))
            errors.rejectValue("name", "obligatorio", "Es obligatorio completar el nombre");

        // Si ya existe el nombre del área natural...
        /*if(naturalAreaDao.getNaturalArea(naturalArea.getName()) != null)
            errors.rejectValue("name", "repetido", "El nombre del área natural ya existe");*/

        // Radio button tipo de acceso comprobar que se haya seleccionado
        if (naturalArea.getTypeOfAccess() == null)
            errors.rejectValue("typeOfAccess", "obligatorio", "Es obligatorio seleccionar el tipo de acceso");
        else {
            // Radio button para tipo de acceso, igual no es necesario //TODO
            List<String> namesTypesOfAccess = Stream.of(TypeOfAccess.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
            if (!namesTypesOfAccess.contains(naturalArea.getTypeOfAccess().name())) {
                errors.rejectValue("typeOfAccess", "incorrecto", "El tipo de acceso es incorrecto");
            }
        }

        // Selecccionar tipo de area, igual no es necesario //TODO
        List<String> namesTypesOfArea = Stream.of(TypeOfArea.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        if (!namesTypesOfArea.contains(naturalArea.getTypeOfArea().name())) {
            errors.rejectValue("typeOfArea", "obligatorio", "No se ha seleccionado un tipo de área");
        }

        // Caracteristicas fisicas obligatorias
        if (naturalArea.getPhysicalCharacteristics().trim().equals(""))
            errors.rejectValue("physicalCharacteristics", "obligatorio", "Es obligatorio completar las características físicas");

        // Descripción obligatoria
        if (naturalArea.getDescription().trim().equals(""))
            errors.rejectValue("description", "obligatorio", "Es obligatorio completar la descripción");

        // Radio button orientacion comprobar que se haya seleccionado
        if (naturalArea.getOrientation() == null)
            errors.rejectValue("orientation", "obligatorio", "Es obligatorio seleccionar la orientación");
        else {
            // Radio button orientacion ver si hay que hacerlo //TODO
            List<String> namesOrientation = Stream.of(Orientation.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
            if (!namesOrientation.contains(naturalArea.getOrientation().name())) {
                errors.rejectValue("orientation", "incorrecto", "La orientación no es correcta");
            }
        }

        // Selecccionar municipio
        // TODO NO VA
        /*if(municipalityDao ==  null) System.out.println("ES NULL");
        List<Municipality> municipalityList = municipalityDao.getMunicipalities();
        List<String> namesMunicipalities = municipalityList.stream()
                .map(Municipality::getName)
                .collect(Collectors.toList());
        if (!namesMunicipalities.contains(naturalArea.getMunicipality())) {
            errors.rejectValue("municipality", "obligatorio", "No se ha seleccionado un municipio");
        }*/
    }
}

