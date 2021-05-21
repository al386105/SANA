package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
import es.uji.ei102720mgph.SANA.model.RegistrationCitizen;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class PerfilValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return RegistrationCitizen.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        RegisteredCitizen registeredCitizen = (RegisteredCitizen) obj;

        // Nombre obligatorio
        if (registeredCitizen.getName().trim().equals(""))
            errors.rejectValue("name", "obligatorio", "Es obligatorio introducir el nombre");

        // Apellidos obligatorios
        if (registeredCitizen.getSurname().trim().equals(""))
            errors.rejectValue("surname", "obligatorio", "Es obligatorio introducir los apellidos");

        // Fecha de nacimiento obligatoria
        if (registeredCitizen.getDateOfBirth() == null)
            errors.rejectValue("dateOfBirth", "obligatorio", "Es obligatorio introducir la fecha de nacimiento");

        // Si es menor de edad...
        LocalDate fechaHace18anyos = LocalDate.now().minusYears(18);
        if (registeredCitizen.getDateOfBirth().isAfter(fechaHace18anyos))
            errors.rejectValue("dateOfBirth", "invalido", "Debe ser mayor de edad para registrarse");

        // Número de teléfono obligatorio
        if (registeredCitizen.getMobilePhoneNumber().trim().equals(""))
            errors.rejectValue("mobilePhoneNumber", "obligatorio", "Es obligatorio introducir el número de teléfono");

        // Número de teléfono numérico
        if(!esNumerico(registeredCitizen.getMobilePhoneNumber()))
            errors.rejectValue("mobilePhoneNumber", "incorrecto", "El número de teléfono debe ser numérico");

        // Número de teléfono de 9 dígitos
        if(registeredCitizen.getMobilePhoneNumber().trim().length() != 9)
            errors.rejectValue("mobilePhoneNumber", "incorrecto", "El número de teléfono debe tener 9 dígitos");

    }

    // devuelve true si la cadena es numérica
    public boolean esNumerico(String cadena) {
        for (int i = 0; i < cadena.length(); i++)
            if (!Character.isDigit(cadena.charAt(i))) return false;
        return true;
    }
}