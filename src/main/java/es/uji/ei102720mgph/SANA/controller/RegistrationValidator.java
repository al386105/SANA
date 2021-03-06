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

        // N??mero de tel??fono obligatorio
        if (registrationCitizen.getTelefono().trim().equals(""))
            errors.rejectValue("telefono", "obligatorio", "Es obligatorio introducir el n??mero de tel??fono");

        // N??mero de tel??fono num??rico
        if(!esNumerico(registrationCitizen.getTelefono()))
            errors.rejectValue("telefono", "incorrecto", "El n??mero de tel??fono debe ser num??rico");

        // N??mero de tel??fono de 9 d??gitos
        if(registrationCitizen.getTelefono().trim().length() != 9)
            errors.rejectValue("telefono", "noCorrecto", "El n??mero de tel??fono debe tener 9 d??gitos");

        // Calle obligatoria
        if (registrationCitizen.getStreet().trim().equals(""))
            errors.rejectValue("street", "obligatorio", "Es obligatorio introducir la calle");

        // N??mero mayor que 0
        if (registrationCitizen.getNumber() < 1)
            errors.rejectValue("number", "incorrecto", "El n??mero debe ser mayor que 0");

        // Piso y puerta obligatorios
        if (registrationCitizen.getFloorDoor().trim().equals(""))
            errors.rejectValue("floorDoor", "obligatorio", "Es obligatorio introducir el piso y puerta");

        // Ciudad obligatoria
        if (registrationCitizen.getCity().trim().equals(""))
            errors.rejectValue("city", "obligatorio", "Es obligatorio introducir la ciudad");

        // C??digo postal obligatoria
        if (registrationCitizen.getPostalCode().trim().equals(""))
            errors.rejectValue("postalCode", "obligatorio", "Es obligatorio introducir el c??digo postal");

        // C??digo postal num??rico
        if(!esNumerico(registrationCitizen.getPostalCode()))
            errors.rejectValue("postalCode", "incorrecto", "El c??digo postal debe ser num??rico");

        // C??digo postal de 5 d??gitos
        if(registrationCitizen.getPostalCode().trim().length() != 5)
            errors.rejectValue("postalCode", "noCorrecto", "El c??digo postal debe tener 5 d??gitos");

        // Pa??s obligatorio
        if (registrationCitizen.getCountry().trim().equals(""))
            errors.rejectValue("country", "obligatorio", "Es obligatorio introducir el pa??s");

        // Contrase??a obligatoria
        if (registrationCitizen.getPassword().trim().equals(""))
            errors.rejectValue("password", "obligatorio", "Es obligatorio introducir la contrase??a");

        // Contrase??a num??rica
        if(!esNumerico(registrationCitizen.getPassword()))
            errors.rejectValue("password", "incorrecto", "La contrase??a es de cuatro d??gitos num??ricos");

        // Contrase??a de 4 d??gitos
        if(registrationCitizen.getPassword().trim().length() != 4)
            errors.rejectValue("password", "noCorrecto", "La contrase??a debe tener 4 d??gitos");

        // Repetir contrase??a obligatorio
        if (registrationCitizen.getPasswordComprovation().trim().equals(""))
            errors.rejectValue("passwordComprovation", "obligatorio", "Por favor, repita la contrase??a");

        // Contrasenya mal escrita
        if (!registrationCitizen.getPassword().trim().equals(registrationCitizen.getPasswordComprovation().trim()))
            errors.rejectValue("passwordComprovation", "incorrecto", "Las contrase??as no coinciden");

    }

    // devuelve true si la cadena es num??rica
    public boolean esNumerico(String cadena) {
        for (int i = 0; i < cadena.length(); i++)
            if (!Character.isDigit(cadena.charAt(i))) return false;
        return true;
    }
}