package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.enums.Temporality;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.model.Municipality;
import es.uji.ei102720mgph.SANA.model.Service;
import es.uji.ei102720mgph.SANA.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/service")
public class ServiceController {

    private ServiceDao serviceDao;

    @Autowired
    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao=serviceDao;
    }

    // Operació llistar
    @RequestMapping("/list")
    public String listServices(Model model, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/service/list");
            return "redirect:/inicio/login";
        }
        if(session.getAttribute("section") != null)
            session.removeAttribute("section");
        model.addAttribute("services", serviceDao.getServices());
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
}
