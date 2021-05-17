package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.RegisteredCitizenDao;
import es.uji.ei102720mgph.SANA.dao.SanaUserDao;
import es.uji.ei102720mgph.SANA.model.RegistrationCitizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RegistrationValidator implements Validator {
    private SanaUserDao sanaUserDao;
    private RegisteredCitizenDao registeredCitizenDao;

    @Override
    public boolean supports(Class<?> cls) {
        return RegistrationCitizen.class.equals(cls);
    }

    @Autowired
    public void setSanaUserDao(SanaUserDao sanaUserDao) { this.sanaUserDao = sanaUserDao; }

    @Autowired
    public void setRegisteredCitizenDao(RegisteredCitizenDao registeredCitizenDao) { this.registeredCitizenDao = registeredCitizenDao; }

    @Override
    public void validate(Object obj, Errors errors) {
        RegistrationCitizen registrationCitizen = (RegistrationCitizen)obj;

        // TODO NO VA
        // Email obligatorio
        /*if (registrationCitizen.getEmail().trim().equals(""))
            errors.rejectValue("email", "obligatorio", "Es obligatorio introducir el email");

        // Si ya existe el email en el sistema...
        if(sanaUserDao.getSanaUser(registrationCitizen.getEmail()) != null)
            errors.rejectValue("email", "repetido", "El email ya está registrado en la apliación");

        // Nombre obligatorio
        if (registrationCitizen.getNombre().trim().equals(""))
            errors.rejectValue("nombre", "obligatorio", "Es obligatorio introducir el nombre");

        // Apellidos obligatorios
        if (registrationCitizen.getApellidos().trim().equals(""))
            errors.rejectValue("apellidos", "obligatorio", "Es obligatorio introducir los apellidos");

        // DNI/NIE obligatorio
        if (registrationCitizen.getDni().trim().equals(""))
            errors.rejectValue("dni", "obligatorio", "Es obligatorio introducir el DNI/NIE");

        // Si ya existe el nie en el sistema...
        if(registeredCitizenDao.getRegisteredCitizenNIE(registrationCitizen.getDni()) != null)
            errors.rejectValue("dni", "repetido", "El DNI/NIE ya está registrado en la apliación");

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

        // Si ya existe el móvil en el sistema...
        if(registeredCitizenDao.getRegisteredCitizenTelf(registrationCitizen.getTelefono()) != null)
            errors.rejectValue("telefono", "repetido", "El teléfono ya está registrado en la apliación");

        // Número de teléfono numérico
        if(!esNumerico(registrationCitizen.getTelefono()))
            errors.rejectValue("telefono", "incorrecto", "El número de teléfono debe ser numérico");

        // Código de ciudadano obligatorio
        if (registrationCitizen.getCitizenCode().trim().equals(""))
            errors.rejectValue("citizenCode", "obligatorio", "Es obligatorio introducir el nombre de usuario");

        // Si ya existe el código de ciudadano en el sistema...
        if(registeredCitizenDao.getRegisteredCitizenCode(registrationCitizen.getCitizenCode()) != null)
            errors.rejectValue("citizenCode", "repetido", "El nombre de usuario ya está registrado en la apliación");

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