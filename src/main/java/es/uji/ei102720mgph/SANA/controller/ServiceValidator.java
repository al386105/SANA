package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Controller
public class ServiceValidator implements Validator {
    private ServiceDao serviceDao;

    @Autowired
    public void setServiceDao(ServiceDao serviceDao) { this.serviceDao = serviceDao; }

    @Override
    public boolean supports(Class<?> cls) {
        return Service.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Service service = (Service)obj;

        // Nombre del servicio obligatorio
        if (service.getNameOfService().trim().equals(""))
            errors.rejectValue("nameOfService", "obligatorio", "Es obligatorio introducir el nombre del servicio");

        // TODO validar temporalidad

        // Descripción obligatoria
        if (service.getDescription().trim().equals(""))
            errors.rejectValue("description", "obligatorio", "Es obligatorio introducir una descripción");

        // Sitio de contratación obligatorio
        if (service.getHiringPlace().trim().equals(""))
            errors.rejectValue("hiringPlace", "obligatorio", "Es obligatorio introducir el sitio de contratación");

        // Si ya existe el nombre del servicio...
        /*if(serviceDao.getService(service.getNameOfService()) != null)
            errors.rejectValue("nameOfService", "repetido", "El nombre del servicio ya existe");*/
    }
}