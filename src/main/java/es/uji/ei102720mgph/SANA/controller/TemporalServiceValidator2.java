package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.TemporalService2;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TemporalServiceValidator2 implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return TemporalService2.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        TemporalService2 temporalService = (TemporalService2) obj;

        // Al menos seleccionar un dia de la semana
        if (temporalService.getDiasMarcados().size() == 0)
            errors.rejectValue("diasMarcados", "obligatorio", "Es obligatorio seleccionar al menos un día de la semana");

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
    }
}