package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class MunicipalManagerValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return MunicipalManager.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        MunicipalManager municipalManager = (MunicipalManager)obj;

        // Nombre de usuario obligatorio
        if (municipalManager.getUsername().trim().equals(""))
            errors.rejectValue("username", "obligatorio", "Es obligatorio introducir el nombre de usuario");

        // Contrasenya obligatoria
        if (municipalManager.getPassword().trim().equals(""))
            errors.rejectValue("password", "obligatorio", "Es obligatorio introducir una contraseña");

        // Email obligatorio
        if (municipalManager.getEmail().trim().equals(""))
            errors.rejectValue("email", "obligatorio", "Es obligatorio introducir el email");

        // Nombre obligatorio
        if (municipalManager.getName().trim().equals(""))
            errors.rejectValue("name", "obligatorio", "Es obligatorio introducir el nombre");

        // Apellido obligatorio
        if (municipalManager.getSurname().trim().equals(""))
            errors.rejectValue("surname", "obligatorio", "Es obligatorio introducir el apellido");

        //  Fecha de nacimiento obligatoria
        if (municipalManager.getDateOfBirth() == null)
            errors.rejectValue("dateOfBirth", "obligatorio", "Es obligatorio introducir la fecha de nacimiento");

        // Si es menor de edad...
        LocalDate fechaHace18anyos = LocalDate.now().minusYears(18);
        if (municipalManager.getDateOfBirth().isAfter(fechaHace18anyos))
            errors.rejectValue("dateOfBirth", "invalido", "El gestor municipal debe ser mayor de edad");
    }
}