package es.uji.ei102720mgph.SANA.controller;


import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    public String listServices(Model model) {
        model.addAttribute("service", serviceDao.getServices());
        return "service/list";
    }

    // Operació crear
    @RequestMapping(value="/add")
    public String addService(Model model) {
        model.addAttribute("service", new Service());
        return "service/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("service") Service service,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "service/add"; //tornem al formulari per a que el corregisca
        serviceDao.addService(service); //usem el dao per a inserir el address
        return "redirect:list"; //redirigim a la lista per a veure el service afegit, post/redirect/get
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{nameOfService}", method = RequestMethod.GET)
    public String editService(Model model, @PathVariable String nameOfService) {

        model.addAttribute("service", serviceDao.getService(nameOfService));
        return "service/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("service") Service service,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "service/update";
        serviceDao.updateService(service);
        return "redirect:list";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{nameOfService}")
    public String processDelete(@PathVariable String nameOfService) {
        serviceDao.deleteService(nameOfService);
        return "redirect:../list";
    }
}
