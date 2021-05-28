package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.dao.ServiceDateDao;
import es.uji.ei102720mgph.SANA.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/serviceDate")
public class ServiceDateController {

    private ServiceDateDao serviceDateDao;
    private ServiceDao serviceDao;

    @Autowired
    public void setServiceDateDao(ServiceDateDao serviceDateDao)  {
        this.serviceDateDao=serviceDateDao;
    }

    @Autowired
    public void setServiceDao(ServiceDao serviceDao)  {
        this.serviceDao=serviceDao;
    }

    // Operació crear
    @RequestMapping(value="/add/{naturalArea}")
    public String addServiceDate(Model model, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/serviceDate/add/" + naturalArea);
            return "redirect:/inicio/login";
        }
        ServiceDate serviceDate = new ServiceDate();
        serviceDate.setNaturalArea(naturalArea);

        // servicios fijos no asignados ya al área natural
        List<Service> toUseServices = serviceDao.getServiceDatesNotInNaturalArea(naturalArea);
        List<String> namesServices = toUseServices.stream()
                .map(Service::getNameOfService)
                .collect(Collectors.toList());
        session.setAttribute("section", "#serviceDates");
        model.addAttribute("serviceList", namesServices);
        model.addAttribute("serviceDate", serviceDate);
        return "serviceDate/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("serviceDate") ServiceDate serviceDate, BindingResult bindingResult) {
        ServiceDateValidator serviceDateValidator = new ServiceDateValidator();
        serviceDateValidator.validate(serviceDate, bindingResult);

        if(model.getAttribute("selector") != null)
            model.addAttribute("selector", null);

        String naturalAreaName = serviceDate.getNaturalArea();
        if (bindingResult.hasErrors()) {
            // servicios fijos no asignados ya al área natural
            List<Service> toUseServices = serviceDao.getServiceDatesNotInNaturalArea(naturalAreaName);
            List<String> namesServices = toUseServices.stream()
                    .map(Service::getNameOfService)
                    .collect(Collectors.toList());
            model.addAttribute("serviceList", namesServices);
            return "serviceDate/add"; //tornem al formulari per a que el corregisca
        }

        try {
            serviceDateDao.addServiceDate(serviceDate);
        } catch (DataIntegrityViolationException e) {
            // selector no seleccionado
            model.addAttribute("selector", "noSeleccionado");
            // servicios fijos no asignados ya al área natural
            List<Service> toUseServices = serviceDao.getServiceDatesNotInNaturalArea(naturalAreaName);
            List<String> namesServices = toUseServices.stream()
                    .map(Service::getNameOfService)
                    .collect(Collectors.toList());
            model.addAttribute("serviceList", namesServices);
            return "serviceDate/add";
        }
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editService(Model model, @PathVariable String id, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/serviceDate/update/" + id);
            return "redirect:/inicio/login";
        }
        session.setAttribute("section", "#serviceDates");
        ServiceDate serviceDate = serviceDateDao.getServiceDate(id);
        // servicios fijos no asignados ya al área natural
        List<Service> toUseServices = serviceDao.getServiceDatesNotInNaturalArea(serviceDate.getNaturalArea());
        List<String> namesServices = toUseServices.stream()
                .map(Service::getNameOfService)
                .collect(Collectors.toList());
        model.addAttribute("serviceList", namesServices);
        model.addAttribute("serviceDate", serviceDate);
        return "serviceDate/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(Model model, @ModelAttribute("serviceDate") ServiceDate serviceDate,
                                      BindingResult bindingResult) {
        ServiceDateValidator serviceDateValidator = new ServiceDateValidator();
        serviceDateValidator.validate(serviceDate, bindingResult);
        String naturalAreaName = serviceDate.getNaturalArea();

        if (bindingResult.hasErrors()) {
            // servicios fijos no asignados ya al área natural
            List<Service> toUseServices = serviceDao.getServiceDatesNotInNaturalArea(naturalAreaName);
            List<String> namesServices = toUseServices.stream()
                    .map(Service::getNameOfService)
                    .collect(Collectors.toList());
            model.addAttribute("serviceList", namesServices);
            return "serviceDate/update";
        }
        serviceDateDao.updateServiceDate(serviceDate);
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }

    // información de un servicio dijo
    @RequestMapping(value="/list/{naturalArea}")
    public String getServiceDateNaturalArea(Model model, @PathVariable String naturalArea, HttpSession session,
                                            @RequestParam(value="patron",required=false) String patron){
        if(session.getAttribute("municipalManager") ==  null && session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/serviceDate/list");
            return "redirect:/inicio/login";
        }
        // Pasar qué tipo de usuario es para mostrar unos botones u otros en la misma vista
        if(session.getAttribute("municipalManager") != null)
            model.addAttribute("typeUser", "manager");
        else if(session.getAttribute("environmentalManager") != null)
            model.addAttribute("typeUser", "environmental");

        session.setAttribute("section", "#serviceDates");
        model.addAttribute("areaNatural", naturalArea);

        // Aplicar filtro
        List<ServiceDate> serviceDates;
        if (patron != null && !patron.equals(""))
            serviceDates = serviceDateDao.getServiceDatesOfNaturalAreaSearch(patron, naturalArea);
        else
            serviceDates = serviceDateDao.getServiceDatesOfNaturalArea(naturalArea);

        //transformarlos a ServiceDateList para que tengan más atributos
        List<ServiceDateList> services = new ArrayList<>();
        for(ServiceDate serviceDate : serviceDates) {
            Service servicio = serviceDao.getService(serviceDate.getService());
            ServiceDateList s = new ServiceDateList();
            s.setNameOfService(serviceDate.getService());
            s.setBeginningDate(serviceDate.getBeginningDate());
            s.setEndDate(serviceDate.getEndDate());
            s.setDescription(servicio.getDescription());
            s.setHiringPlace(servicio.getHiringPlace());
            services.add(s);
        }
        model.addAttribute("services", services);
        return "/serviceDate/list";
    }

    // Dar de baja servicio fijo
    @RequestMapping(value="/darDeBaja/{id}", method = RequestMethod.GET)
    public String darDeBajaServiceDate(Model model, @PathVariable String id, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/serviceDate/darDeBaja/" + id);
            return "redirect:/inicio/login";
        }
        ServiceDate serviceDate = serviceDateDao.getServiceDate(id);
        serviceDate.setEndDate(LocalDate.now());
        String naturalAreaName = serviceDate.getNaturalArea();
        serviceDateDao.updateServiceDate(serviceDate);
        session.setAttribute("section", "#serviceDates");
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }
}
