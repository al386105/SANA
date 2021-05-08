package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.model.Service;
import es.uji.ei102720mgph.SANA.model.ServiceDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceDateValidator implements Validator {
    private ServiceDao serviceDao;

    @Autowired
    public void setServiceDao(ServiceDao serviceDao) { this.serviceDao = serviceDao; }

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

        // Orden de fechas si hay fecha de fin (opcional)
        if (serviceDate.getEndDate() != null && serviceDate.getBeginningDate() != null
                && serviceDate.getBeginningDate().isAfter(serviceDate.getEndDate()))
            errors.rejectValue("endDate", "valor incorrecto", "La fecha de fin debe ser posterior a la fecha de inicio");

        /* TODO no va
        // Seleccionar servicio
        List<Service> serviceList = serviceDao.getServices();
        List<String> namesServices = serviceList.stream()
                .map(Service::getNameOfService)
                .collect(Collectors.toList());
        if (!namesServices.contains(serviceDate.getService())) {
            errors.rejectValue("serviceDate", "valor incorrecto",
                    "No se ha seleccionado un servicio");
        }*/
    }
}


