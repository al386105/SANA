package es.uji.ei102720mgph.SANA.controller;


import es.uji.ei102720mgph.SANA.dao.ServiceDateDao;
import es.uji.ei102720mgph.SANA.model.ServiceDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/serviceDate")
public class ServiceDateController {

    private ServiceDateDao serviceDateDao;

    @Autowired
    public void setServiceDateDao(ServiceDateDao serviceDateDao)  {
        this.serviceDateDao=serviceDateDao;
    }

    // Operació llistar
    @RequestMapping("/list")
    public String listServiceDates(Model model) {
        model.addAttribute("serviceDate", serviceDateDao.getServiceDates());
        return "serviceDate/list";
    }

    // Operació crear
    @RequestMapping(value="/add")
    public String addServiceDate(Model model) {
        model.addAttribute("serviceDate", new ServiceDate());
        return "serviceDate/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("serviceDate") ServiceDate serviceDate,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "serviceDate/add"; //tornem al formulari per a que el corregisca
        serviceDateDao.addServiceDate(serviceDate); //usem el dao per a inserir el address
        return "redirect:list"; //redirigim a la lista per a veure el service afegit, post/redirect/get
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editService(Model model, @PathVariable String id) {
        model.addAttribute("serviceDate", serviceDateDao.getServiceDate(id));
        return "serviceDate/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("serviceDate") ServiceDate serviceDate,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "serviceDate/update";
        serviceDateDao.updateServiceDate(serviceDate);
        return "redirect:list";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable String id) {
        serviceDateDao.deleteServiceDate(id);
        return "redirect:../list";
    }
}
