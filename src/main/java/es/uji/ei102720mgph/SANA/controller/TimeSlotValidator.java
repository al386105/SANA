package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.TimeSlot;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TimeSlotValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return TimeSlot.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        TimeSlot timeSlot = (TimeSlot) obj;

        // Fecha de inicio obligatoria
        if (timeSlot.getBeginningDate() == null)
            errors.rejectValue("beginningDate", "obligatorio", "Es obligatorio introducir la fecha de inicio");

        // Fecha de fin obligatoria
        if (timeSlot.getEndDate() == null)
            errors.rejectValue("endDate", "obligatorio", "Es obligatorio introducir la fecha de fin");

        // Orden de fechas
        if (timeSlot.getEndDate() != null && timeSlot.getBeginningDate() != null
                && timeSlot.getBeginningDate().isAfter(timeSlot.getEndDate()))
            errors.rejectValue("endDate", "valor incorrecto", "La fecha de fin debe ser posterior a la fecha de inicio");

        // Hora de inicio obligatoria
        if (timeSlot.getBeginningTime() == null)
            errors.rejectValue("beginningTime", "obligatorio", "Es obligatorio introducir la hora de inicio");

        // Hora de fin obligatoria
        if (timeSlot.getEndTime() == null)
            errors.rejectValue("endTime", "obligatorio", "Es obligatorio introducir la hora de fin");

        // Orden de horas
        if (timeSlot.getBeginningTime() != null && timeSlot.getEndTime() != null &&
                timeSlot.getBeginningTime().isAfter(timeSlot.getEndTime()))
            errors.rejectValue("endTime", "valor incorrecto", "La hora de fin debe ser posterior a la hora de inicio");
    }
}
