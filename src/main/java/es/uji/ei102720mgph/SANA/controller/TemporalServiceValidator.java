package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.model.Service;
import es.uji.ei102720mgph.SANA.model.ServiceDate;
import es.uji.ei102720mgph.SANA.model.TemporalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import es.uji.ei102720mgph.SANA.model.NaturalArea;

import java.time.temporal.Temporal;
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

        List<Service> serviceList = serviceDao.getServices();
        List<String> namesServices = serviceList.stream()
                .map(Service::getNameOfService)
                .collect(Collectors.toList());

        if (!namesServices.contains(temporalService.getService())) {
            errors.rejectValue("temporalService", "valor incorrecto",
                    "No se ha seleccionado un servicio");
        }
    }
}