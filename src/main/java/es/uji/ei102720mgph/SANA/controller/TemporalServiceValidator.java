package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.model.Service;
import es.uji.ei102720mgph.SANA.model.TemporalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class TemporalServiceValidator implements Validator {
    private ServiceDao serviceDao;

    @Autowired
    public void setService(ServiceDao serviceDao) { this.serviceDao = serviceDao; }

    @Override
    public boolean supports(Class<?> cls) {
        return TemporalService.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        TemporalService temporalService = (TemporalService) obj;

        /*
        //Seleccionar servicio
        List<Service> serviceList = serviceDao.getServices();
        List<String> namesServices = serviceList.stream()
                .map(Service::getNameOfService)
                .collect(Collectors.toList());

        if (!namesServices.contains(temporalService.getService())) {
            errors.rejectValue("temporalService", "valor incorrecto",
                    "No se ha seleccionado un servicio");
        }*/

        // Dias de apertura obligatorio
        if (temporalService.getOpeningDays().trim().equals(""))
            errors.rejectValue("openingDays", "obligatorio", "Es obligatorio introducir los días de apertura");

        // Hora de inicio obligatoria
        if (temporalService.getBeginningTime() == null)
            errors.rejectValue("beginningTime", "obligatorio", "Es obligatorio introducir la hora de inicio");

        // Hora de fin obligatoria
        if (temporalService.getEndTime() == null)
            errors.rejectValue("endTime", "obligatorio", "Es obligatorio introducir la hora de fin");

        // Orden de horas
        if (temporalService.getBeginningTime() != null && temporalService.getEndTime() != null &&
                temporalService.getBeginningTime().isAfter(temporalService.getEndTime()))
            errors.rejectValue("endTime", "valor incorrecto", "La hora de inicio debe ser anterior a la hora de fin");

        // Fecha de inicio obligatoria
        if (temporalService.getBeginningDate() == null)
            errors.rejectValue("beginningDate", "obligatorio", "Es obligatorio introducir la fecha de inicio");

        // Orden de fechas si hay fecha de fin (opcional)
        if (temporalService.getEndDate() != null && temporalService.getBeginningDate() != null
                && temporalService.getBeginningDate().isAfter(temporalService.getEndDate()))
            errors.rejectValue("endDate", "valor incorrecto", "La fecha de inicio debe ser anterior a la fecha de fin");

    }
}