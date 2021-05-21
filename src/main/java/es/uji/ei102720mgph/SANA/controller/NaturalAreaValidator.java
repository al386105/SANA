package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.enums.Orientation;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.enums.TypeOfArea;
import es.uji.ei102720mgph.SANA.model.NaturalAreaForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NaturalAreaValidator implements Validator {

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

        // Radio button tipo de acceso comprobar que se haya seleccionado
        if (naturalArea.getTypeOfAccess() == null)
            errors.rejectValue("typeOfAccess", "obligatorio", "Es obligatorio seleccionar el tipo de acceso");
        else {
            // Radio button para tipo de acceso
            List<String> namesTypesOfAccess = Stream.of(TypeOfAccess.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
            if (!namesTypesOfAccess.contains(naturalArea.getTypeOfAccess().name())) {
                errors.rejectValue("typeOfAccess", "incorrecto", "El tipo de acceso es incorrecto");
            }
        }

        // Latitud grados
        if (naturalArea.getLatitudGrados() < 0.0)
            errors.rejectValue("latitudGrados", "incorrecto", "Los grados de latitud deben ser un número superior a 0");

        // Latitud minutos
        if (naturalArea.getLongitudMin() < 0.0)
            errors.rejectValue("latitudMin", "incorrecto", "Los minutos de latitud deben ser un número superior a 0");

        // Latitud segundos
        if (naturalArea.getLatitudSeg() < 0.0)
            errors.rejectValue("latitudSeg", "incorrecto", "Los segundos de latitud deben ser un número superior a 0");

        // Selecccionar letra latitud
        if (!naturalArea.getLatitudLetra().trim().equals("N") & !naturalArea.getLatitudLetra().trim().equals("S")) {
            errors.rejectValue("latitudLetra", "obligatorio", "Seleccionar Norte o Sur");
        }

        // Longitud grados
        if (naturalArea.getLongitudGrados() < 0.0)
            errors.rejectValue("longitudGrados", "incorrecto", "Los grados de longitud deben ser un número superior a 0");

        // Longitud minutos
        if (naturalArea.getLongitudMin() < 0.0)
            errors.rejectValue("longitudMin", "incorrecto", "Los minutos de longitud deben ser un número superior a 0");

        // Longitud segundos
        if (naturalArea.getLongitudSeg() < 0.0)
            errors.rejectValue("longitudSeg", "incorrecto", "Los segundos de longitud deben ser un número superior a 0");

        // Selecccionar letra longitud
        if (!naturalArea.getLongitudLetra().trim().equals("E") & !naturalArea.getLongitudLetra().trim().equals("W")) {
            errors.rejectValue("longitudLetra", "obligatorio", "Seleccionar Este u Oeste");
        }

        // Selecccionar tipo de area, igual no es necesario
        List<String> namesTypesOfArea = Stream.of(TypeOfArea.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        if (!namesTypesOfArea.contains(naturalArea.getTypeOfArea().name())) {
            errors.rejectValue("typeOfArea", "obligatorio", "No se ha seleccionado un tipo de área");
        }

        // Longitud positiva
        if (naturalArea.getLength() < 0.0)
            errors.rejectValue("length", "incorrecto", "La longitud debe ser un número superior a 0");

        // Ancho positivo
        if (naturalArea.getWidth() < 0.0)
            errors.rejectValue("width", "incorrecto", "El ancho debe ser un número superior a 0");

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
            // Radio button orientacion ver si hay que hacerlo
            List<String> namesOrientation = Stream.of(Orientation.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
            if (!namesOrientation.contains(naturalArea.getOrientation().name())) {
                errors.rejectValue("orientation", "incorrecto", "La orientación no es correcta");
            }
        }
    }
}

