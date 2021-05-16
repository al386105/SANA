package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.MunicipalManagerDao;
import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.dao.SanaUserDao;
import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import es.uji.ei102720mgph.SANA.model.SanaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class MunicipalManagerValidator implements Validator {
    private MunicipalManagerDao municipalManagerDao;
    private SanaUserDao sanaUserDao;

    @Override
    public boolean supports(Class<?> cls) {
        return MunicipalManager.class.equals(cls);
    }

    @Autowired
    public void setMunicipalManagerDao(MunicipalManagerDao municipalManagerDao) { this.municipalManagerDao = municipalManagerDao; }

    @Autowired
    public void setSanaUserDao(SanaUserDao sanaUserDao) { this.sanaUserDao = sanaUserDao; }

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

        // TODO no va
        /*// Si ya existe el email en el sistema...
        if(sanaUserDao.getSanaUser(municipalManager.getEmail()) != null)
            errors.rejectValue("email", "repetido", "El email ya está registrado en la apliación");
        // Si ya existe el username en el sistema...
        if(municipalManagerDao.getMunicipalManagerUsername(municipalManager.getUsername()) != null)
            errors.rejectValue("username", "repetido", "El nombre de usuario asignado ya existe");*/

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