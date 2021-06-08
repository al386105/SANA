package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.ServiceDate;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

public class ServiceDateValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return ServiceDate.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        ServiceDate serviceDate = (ServiceDate)obj;

        // Fecha de inicio obligatoria
        if (serviceDate.getBeginningDate() == null)
            errors.rejectValue("beginningDate", "obligatorio", "Es obligatorio introducir la fecha de inicio");

        // Fecha de inicio posterior a hoy
        if (serviceDate.getBeginningDate() != null && serviceDate.getBeginningDate().isBefore(LocalDate.now()))
            errors.rejectValue("beginningDate", "incorrecto", "La fecha de inicio debe ser posterior a hoy");

        // Orden de fechas si hay fecha de fin (opcional)
        if (serviceDate.getEndDate() != null && serviceDate.getBeginningDate() != null
                && serviceDate.getBeginningDate().isAfter(serviceDate.getEndDate()))
            errors.rejectValue("endDate", "valor incorrecto", "La fecha de fin debe ser posterior a la fecha de inicio");
    }
}


