package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.dao.ServiceDateDao;
import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import es.uji.ei102720mgph.SANA.model.Service;
import es.uji.ei102720mgph.SANA.model.ServiceDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
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

    // Operació llistar
    @RequestMapping("/list")
    public String listServiceDates(Model model) {
        model.addAttribute("serviceDate", serviceDateDao.getServiceDates());
        return "serviceDate/list";
    }

    // metodo para anyadir al modelo los datos del selector
    @ModelAttribute("serviceList")
    public List<String> serviceList() {
        List<Service> serviceList = serviceDao.getFixedServices();
        List<String> namesServices = serviceList.stream()
                .map(Service::getNameOfService)
                .collect(Collectors.toList());
        return namesServices;
    }

    // Operació crear
    @RequestMapping(value="/add/{naturalArea}")
    public String addServiceDate(Model model, @PathVariable String naturalArea) {
        ServiceDate serviceDate = new ServiceDate();
        serviceDate.setNaturalArea(naturalArea);
        model.addAttribute("serviceDate", serviceDate);
        return "serviceDate/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("serviceDate") ServiceDate serviceDate,
                                   BindingResult bindingResult) {
        ServiceDateValidator serviceDateValidator = new ServiceDateValidator();
        serviceDateValidator.validate(serviceDate, bindingResult);

        if (bindingResult.hasErrors())
            return "serviceDate/add"; //tornem al formulari per a que el corregisca
        String naturalAreaName = serviceDate.getNaturalArea();
        serviceDateDao.addServiceDate(serviceDate); //usem el dao per a inserir el address
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
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
        ServiceDateValidator serviceDateValidator = new ServiceDateValidator();
        serviceDateValidator.validate(serviceDate, bindingResult);

        if (bindingResult.hasErrors())
            return "serviceDate/update";
        serviceDateDao.updateServiceDate(serviceDate);
        String naturalAreaName = serviceDate.getNaturalArea();
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable String id) {
        ServiceDate serviceDate = serviceDateDao.getServiceDate(id);
        String naturalAreaName = serviceDate.getNaturalArea();
        serviceDateDao.deleteServiceDate(id);
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }

    // información de un servicio dijo
    @RequestMapping(value="/get/{id}")
    public String getServiceDateNaturalArea(Model model, @PathVariable String id){
        ServiceDate serviceDate = serviceDateDao.getServiceDate(id);
        model.addAttribute("serviceDate", serviceDateDao.getServiceDate(id));
        model.addAttribute("service", serviceDao.getService(serviceDate.getService()));
        return "/serviceDate/get";
    }

    @RequestMapping(value="/darDeBaja/{id}", method = RequestMethod.GET)
    public String darDeBajaServiceDate(@PathVariable String id) {
        ServiceDate serviceDate = serviceDateDao.getServiceDate(id);
        serviceDate.setEndDate(LocalDate.now());
        String naturalAreaName = serviceDate.getNaturalArea();
        serviceDateDao.updateServiceDate(serviceDate);
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }
}
