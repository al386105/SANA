package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.EmailDao;
import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.dao.ServiceDateDao;
import es.uji.ei102720mgph.SANA.dao.TemporalServiceDao;
import es.uji.ei102720mgph.SANA.enums.Temporality;
import es.uji.ei102720mgph.SANA.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static es.uji.ei102720mgph.SANA.controller.HomeController.enviarMail;

@Controller
@RequestMapping("/service")
public class ServiceController {
    private ServiceDateDao serviceDateDao;
    private ServiceDao serviceDao;
    private TemporalServiceDao temporalServiceDao;
    private EmailDao emailDao;

    @Autowired
    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    @Autowired
    public void setEmailDao(EmailDao emailDao) {
        this.emailDao = emailDao;
    }

    @Autowired
    public void setServiceDateDao(ServiceDateDao serviceDateDao) {
        this.serviceDateDao = serviceDateDao;
    }

    @Autowired
    public void setTemporalServiceDao(TemporalServiceDao temporalServiceDao) {
        this.temporalServiceDao = temporalServiceDao;
    }

    // Operació llistar
    @RequestMapping("/list")
    public String listServices(Model model, HttpSession session, @RequestParam(value="patron",required=false) String patron,
                               @RequestParam(value="temporalidad",required=false) String temporalidad) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/service/list");
            return "redirect:/inicio/login";
        }
        if(session.getAttribute("section") != null)
            session.removeAttribute("section");

        // Aplicar filtro
        List<Service> services;
        if (patron != null && !patron.equals(""))
            services = serviceDao.getServiceSearch(patron);
        else
            services = serviceDao.getServices();

        // filtro por temporalidad
        if (temporalidad != null && !temporalidad.equals("todas"))
            services.removeIf(service -> service.getTemporality() != Temporality.valueOf(temporalidad));

        model.addAttribute("services", services);
        return "service/list";
    }

    // Operació crear
    @RequestMapping(value="/add")
    public String addService(Model model, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/service/add");
            return "redirect:/inicio/login";
        }
        model.addAttribute("service", new Service());
        return "service/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("service") Service service, BindingResult bindingResult) {
        ServiceValidator serviceValidator = new ServiceValidator();
        serviceValidator.validate(service, bindingResult);
        if(model.getAttribute("claveRepetida") != null)
            model.addAttribute("claveRepetida", null);

        if (bindingResult.hasErrors())
            return "service/add"; //tornem al formulari per a que el corregisca

        try {
            serviceDao.addService(service);
        } catch (DataIntegrityViolationException e) {
            if(serviceDao.getService(service.getNameOfService()) != null)
                model.addAttribute("claveRepetida", "repetida");
            return "service/add";
        }
        return "redirect:list"; //redirigim a la lista per a veure el service afegit, post/redirect/get
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{nameOfService}", method = RequestMethod.GET)
    public String editService(Model model, @PathVariable String nameOfService, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/service/update/" + nameOfService);
            return "redirect:/inicio/login";
        }
        model.addAttribute("service", serviceDao.getService(nameOfService));
        return "service/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(Model model, @ModelAttribute("service") Service service,
                                      BindingResult bindingResult) {
        ServiceValidator serviceValidator = new ServiceValidator();
        serviceValidator.validate(service, bindingResult);
        if (bindingResult.hasErrors())
            return "service/update";
        serviceDao.updateService(service);
        return "redirect:list";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{nameOfService}")
    public String processDelete(Model model, @PathVariable String nameOfService, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/service/delete/" + nameOfService);
            return "redirect:/inicio/login";
        }
        serviceDao.deleteService(nameOfService);
        return "redirect:../list";
    }

    // Solicitar crear un servicio al responsable
    @RequestMapping(value="/solicitar/{naturalArea}")
    public String processSolicitarServicio(@PathVariable String naturalArea, Service service) {
        // Enviar mensaje al responsable para solicitar el servicio
        String destinatario = "responsablesana1@gmail.com";
        String asunto = "Solicitud de servicio";
        String cuerpo = "Se ha solicitado crear el servicio de " + service.getNameOfService() + " para poder asignarlo al área natural " +
                naturalArea + ".";
        Email email = enviarMail(destinatario, asunto, cuerpo);
        emailDao.addEmail(email);

        return "redirect:/service/getForManagersServices/" + naturalArea;
    }

    @RequestMapping(value="/getForManagersServices/{naturalArea}")
    public String getServicesForManagers(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/service/getForManagersServices/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalArea);
        serviceDateLista(model, naturalArea);
        model.addAttribute("temporalServices", temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea));
        model.addAttribute("serviceDatesFaltan", serviceDao.getServiceDatesNotInNaturalArea(naturalArea));
        model.addAttribute("service", new Service());

        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/service/getForManagersServices/" + naturalArea + section;
        }
        return "/service/getForManagersServices";
    }

    @RequestMapping(value="/getForEnvironmentalServices/{naturalArea}")
    public String getForEnvironmentalServices(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/service/getForEnvironmentalServices/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalArea);
        model.addAttribute("temporalServices", temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea));
        serviceDateLista(model, naturalArea);
        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/service/getForEnvironmentalServices/" + naturalArea + section;
        }
        return "/service/getForEnvironmentalServices";
    }


    private void serviceDateLista(Model model, String naturalArea) {
        //transformar los serviceDates a ServiceDateList para que tengan más atributos (los de las tablas)
        List<ServiceDate> serviceDates = serviceDateDao.getServiceDatesOfNaturalAreaOperativos(naturalArea);
        List<ServiceDateList> services = new ArrayList<>();
        for(ServiceDate serviceDate : serviceDates) {
            Service servicio = serviceDao.getService(serviceDate.getService());
            ServiceDateList s = new ServiceDateList();
            s.setNameOfService(serviceDate.getService());
            s.setBeginningDate(serviceDate.getBeginningDate());
            s.setDescription(servicio.getDescription());
            s.setHiringPlace(servicio.getHiringPlace());
            s.setId(serviceDate.getId());
            services.add(s);
        }
        model.addAttribute("serviceDates", services);
    }
}
