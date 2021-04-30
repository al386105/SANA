package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.dao.TemporalServiceDao;
import es.uji.ei102720mgph.SANA.model.Service;
import es.uji.ei102720mgph.SANA.model.TemporalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/temporalService")
public class TemporalServiceController {

    private TemporalServiceDao temporalServiceDao;
    private ServiceDao serviceDao;

    @Autowired
    public void setTemporalServiceDao(TemporalServiceDao temporalServiceDao) {
        this.temporalServiceDao=temporalServiceDao;
    }

    @Autowired
    public void setServiceDao(ServiceDao serviceDao) {
        this.serviceDao=serviceDao;
    }

    // Operació llistar
    @RequestMapping("/list")
    public String listTemporalServices(Model model) {
        model.addAttribute("temporalService", temporalServiceDao.getTemporalServices());
        return "temporalService/list";
    }

    // Operació crear
    @RequestMapping(value="/add/{naturalArea}")
    public String addTemporalService(Model model, @PathVariable String naturalArea) {
        TemporalService temporalService = new TemporalService();
        temporalService.setNaturalArea(naturalArea);
        model.addAttribute("temporalService", temporalService);
        List<Service> serviceList = serviceDao.getTemporalServices();
        List<String> namesServices = serviceList.stream()
                .map(Service::getNameOfService)
                .collect(Collectors.toList());
        model.addAttribute("serviceList", namesServices);
        return "temporalService/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("temporalService") TemporalService temporalService,
                                   BindingResult bindingResult) {
        TemporalServiceValidator temporalServiceValidator = new TemporalServiceValidator();
        temporalServiceValidator.validate(temporalServiceValidator, bindingResult);

        if (bindingResult.hasErrors())
            return "temporalService/add"; //tornem al formulari per a que el corregisca
        temporalServiceDao.addTemporalService(temporalService); //usem el dao per a inserir el temporalService
        String naturalAreaName = temporalService.getNaturalArea();
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{service}/{naturalArea}", method = RequestMethod.GET)
    public String editTemporalService(Model model, @PathVariable String service, @PathVariable String naturalArea) {
        model.addAttribute("temporalService", temporalServiceDao.getTemporalService(service, naturalArea));
        List<Service> serviceList = serviceDao.getTemporalServices();
        List<String> namesServices = serviceList.stream()
                .map(Service::getNameOfService)
                .collect(Collectors.toList());
        model.addAttribute("serviceList", namesServices);
        return "temporalService/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("temporalService") TemporalService temporalService,
                                      BindingResult bindingResult) {
        TemporalServiceValidator temporalServiceValidator = new TemporalServiceValidator();
        temporalServiceValidator.validate(temporalServiceValidator, bindingResult);

        if (bindingResult.hasErrors())
            return "temporalService/update";
        temporalServiceDao.updateTemporalService(temporalService);
        String naturalAreaName = temporalService.getNaturalArea();
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{service}/{naturalArea}")
    public String processDelete(@PathVariable String service, @PathVariable String naturalArea) {
        TemporalService temporalService = temporalServiceDao.getTemporalService(service, naturalArea);
        String naturalAreaName = temporalService.getNaturalArea();
        temporalServiceDao.deleteTemporalService(service, naturalArea);
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }
}
