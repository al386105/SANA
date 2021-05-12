package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.RegistrationCitizen;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RegistrationValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return RegistrationCitizen.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        RegistrationCitizen registrationCitizen = (RegistrationCitizen)obj;
        /*
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

        // Fecha de nacimiento obligatoria
        if (registrationCitizen.getDateOfBirth() == null)
            errors.rejectValue("dateOfBirth", "obligatorio", "Es obligatorio introducir la fecha de nacimiento");

        // Número de teléfono obligatorio
        if (registrationCitizen.getTelefono().trim().equals(""))
            errors.rejectValue("telefono", "obligatorio", "Es obligatorio introducir el número de teléfono");

        // Número de teléfono numérico
        if(!esNumerico(registrationCitizen.getTelefono()))
            errors.rejectValue("telefono", "incorrecto", "El número de teléfono debe ser numérico");

        // Código de ciudadano obligatorio
        if (registrationCitizen.getCitizenCode().trim().equals(""))
            errors.rejectValue("citizenCode", "obligatorio", "Es obligatorio introducir el nombre de usuario");

        // Calle obligatoria
        if (registrationCitizen.getStreet().trim().equals(""))
            errors.rejectValue("street", "obligatorio", "Es obligatorio introducir la calle");

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

        // País obligatorio
        if (registrationCitizen.getCountry().trim().equals(""))
            errors.rejectValue("country", "obligatorio", "Es obligatorio introducir el país");

        // Contraseña obligatoria
        if (registrationCitizen.getPassword().trim().equals(""))
            errors.rejectValue("password", "obligatorio", "Es obligatorio introducir la contraseña");

        // Contraseña numérica
        if(!esNumerico(registrationCitizen.getPassword()))
            errors.rejectValue("password", "incorrecto", "La contraseña es de cuatro dígitos numéricos");

         */
    }

    // devuelve true si la cadena es numérica
    public boolean esNumerico(String cadena) {
        for (int i = 0; i < cadena.length(); i++)
            if (!Character.isDigit(cadena.charAt(i))) return false;
        return true;
    }
}