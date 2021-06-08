package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.RegistrationCitizen;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class RegistrationValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return RegistrationCitizen.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        RegistrationCitizen registrationCitizen = (RegistrationCitizen) obj;

        // Email obligatorio
        if (registrationCitizen.getEmail().trim().equals(""))
            errors.rejectValue("email", "obligatorio", "Es obligatorio introducir el email");

        // Nombre obligatorio
        if (registrationCitizen.getNombre().trim().equals(""))
            errors.rejectValue("nombre", "obligatorio", "Es obligatorio introducir el nombre");

        // Apellidos obligatorios
        if (registrationCitizen.getApellidos().trim().equals(""))
            errors.rejectValue("apellidos", "obligatorio", "Es obligatorio introducir los apellidos");

        // DNI/NIE obligatorio
        if (registrationCitizen.getDni().trim().equals(""))
            errors.rejectValue("dni", "obligatorio", "Es obligatorio introducir el DNI/NIE");

        // DNI/NIE de 9 caracteres
        if (registrationCitizen.getDni().trim().length() != 9)
            errors.rejectValue("dni", "obligatorio", "El DNI/NIE tiene 9 caracteres");

        // Fecha de nacimiento obligatoria
        if (registrationCitizen.getDateOfBirth() == null)
            errors.rejectValue("dateOfBirth", "obligatorio", "Es obligatorio introducir la fecha de nacimiento");

        // Si es menor de edad...
        LocalDate fechaHace18anyos = LocalDate.now().minusYears(18);
        if (registrationCitizen.getDateOfBirth().isAfter(fechaHace18anyos))
            errors.rejectValue("dateOfBirth", "invalido", "Debe ser mayor de edad para registrarse");

        // Número de teléfono obligatorio
        if (registrationCitizen.getTelefono().trim().equals(""))
            errors.rejectValue("telefono", "obligatorio", "Es obligatorio introducir el número de teléfono");

        // Número de teléfono numérico
        if(!esNumerico(registrationCitizen.getTelefono()))
            errors.rejectValue("telefono", "incorrecto", "El número de teléfono debe ser numérico");

        // Número de teléfono de 9 dígitos
        if(registrationCitizen.getTelefono().trim().length() != 9)
            errors.rejectValue("telefono", "noCorrecto", "El número de teléfono debe tener 9 dígitos");

        // Calle obligatoria
        if (registrationCitizen.getStreet().trim().equals(""))
            errors.rejectValue("street", "obligatorio", "Es obligatorio introducir la calle");

        // Número mayor que 0
        if (registrationCitizen.getNumber() < 1)
            errors.rejectValue("number", "incorrecto", "El número debe ser mayor que 0");

        // Piso y puerta obligatorios
        if (registrationCitizen.getFloorDoor().trim().equals(""))
            errors.rejectValue("floorDoor", "obligatorio", "Es obligatorio introducir el piso y puerta");

        // Ciudad obligatoria
        if (registrationCitizen.getCity().trim().equals(""))
            errors.rejectValue("city", "obligatorio", "Es obligatorio introducir la ciudad");

        // Código postal obligatoria
        if (registrationCitizen.getPostalCode().trim().equals(""))
            errors.rejectValue("postalCode", "obligatorio", "Es obligatorio introducir el código postal");

        // Código postal numérico
        if(!esNumerico(registrationCitizen.getPostalCode()))
            errors.rejectValue("postalCode", "incorrecto", "El código postal debe ser numérico");

        // Código postal de 5 dígitos
        if(registrationCitizen.getPostalCode().trim().length() != 5)
            errors.rejectValue("postalCode", "noCorrecto", "El código postal debe tener 5 dígitos");

        // País obligatorio
        if (registrationCitizen.getCountry().trim().equals(""))
            errors.rejectValue("country", "obligatorio", "Es obligatorio introducir el país");

        // Contraseña obligatoria
        if (registrationCitizen.getPassword().trim().equals(""))
            errors.rejectValue("password", "obligatorio", "Es obligatorio introducir la contraseña");

        // Contraseña numérica
        if(!esNumerico(registrationCitizen.getPassword()))
            errors.rejectValue("password", "incorrecto", "La contraseña es de cuatro dígitos numéricos");

        // Contraseña de 4 dígitos
        if(registrationCitizen.getPassword().trim().length() != 4)
            errors.rejectValue("password", "noCorrecto", "La contraseña debe tener 4 dígitos");

        // Repetir contraseña obligatorio
        if (registrationCitizen.getPasswordComprovation().trim().equals(""))
            errors.rejectValue("passwordComprovation", "obligatorio", "Por favor, repita la contraseña");

        // Contrasenya mal escrita
        if (!registrationCitizen.getPassword().trim().equals(registrationCitizen.getPasswordComprovation().trim()))
            errors.rejectValue("passwordComprovation", "incorrecto", "Las contraseñas no coinciden");

    }

    // devuelve true si la cadena es numérica
    public boolean esNumerico(String cadena) {
        for (int i = 0; i < cadena.length(); i++)
            if (!Character.isDigit(cadena.charAt(i))) return false;
        return true;
    }
}