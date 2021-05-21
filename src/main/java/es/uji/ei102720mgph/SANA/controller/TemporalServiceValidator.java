package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.TemporalService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TemporalServiceValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return TemporalService.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        TemporalService temporalService = (TemporalService) obj;

        // Hora de inicio obligatoria
        if (temporalService.getBeginningTime() == null)
            errors.rejectValue("beginningTime", "obligatorio", "Es obligatorio introducir la hora de inicio");

        // Hora de fin obligatoria
        if (temporalService.getEndTime() == null)
            errors.rejectValue("endTime", "obligatorio", "Es obligatorio introducir la hora de fin");

        // Orden de horas
        if (temporalService.getBeginningTime() != null && temporalService.getEndTime() != null &&
                temporalService.getBeginningTime().isAfter(temporalService.getEndTime()))
            errors.rejectValue("endTime", "valor incorrecto", "La hora de fin debe ser posterior a la hora de inicio");

        // Fecha de inicio obligatoria
        if (temporalService.getBeginningDate() == null)
            errors.rejectValue("beginningDate", "obligatorio", "Es obligatorio introducir la fecha de inicio");

        // Orden de fechas si hay fecha de fin (opcional)
        if (temporalService.getEndDate() != null && temporalService.getBeginningDate() != null
                && temporalService.getBeginningDate().isAfter(temporalService.getEndDate()))
            errors.rejectValue("endDate", "valor incorrecto", "La fecha de fin debe ser posterior a la fecha de inicio");

        // Al menos seleccionar un dia de la semana
        if (temporalService.getOpeningDays() == null)
            errors.rejectValue("openingDays", "obligatorio", "Es obligatorio seleccionar al menos un día de la semana");
    }
}