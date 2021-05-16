package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.dao.TemporalServiceDao;
import es.uji.ei102720mgph.SANA.enums.DaysOfWeek;
import es.uji.ei102720mgph.SANA.model.Service;
import es.uji.ei102720mgph.SANA.model.TemporalService;
import es.uji.ei102720mgph.SANA.model.TemporalService2;
import es.uji.ei102720mgph.SANA.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
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

    // metodos para anyadir al modelo los datos del selector o radio buttons
    @ModelAttribute("serviceList")
    public List<String> serviceList() {
        // TODO deberían devolverse sólo los servicios no asignados al área
        List<Service> serviceList = serviceDao.getTemporalServices();
        List<String> namesServices = serviceList.stream()
                .map(Service::getNameOfService)
                .collect(Collectors.toList());
        return namesServices;
    }

    // Operació crear
    @RequestMapping(value="/add/{naturalArea}")
    public String addTemporalService(Model model, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/temporalService/add/" + naturalArea);
            return "/inicio/login";
        }
        TemporalService temporalService = new TemporalService();
        temporalService.setNaturalArea(naturalArea);
        model.addAttribute("temporalService", temporalService);
        return "temporalService/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("temporalService") TemporalService temporalService,
                                   BindingResult bindingResult) {
        TemporalServiceValidator temporalServiceValidator = new TemporalServiceValidator();
        temporalServiceValidator.validate(temporalService, bindingResult);

        if (bindingResult.hasErrors())
            return "temporalService/add"; //tornem al formulari per a que el corregisca
        temporalServiceDao.addTemporalService(temporalService); //usem el dao per a inserir el temporalService
        String naturalAreaName = temporalService.getNaturalArea();
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{service}/{naturalArea}", method = RequestMethod.GET)
    public String editTemporalService(Model model, @PathVariable String service, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/temporalService/update/" + service + "/" + naturalArea);
            return "/inicio/login";
        }
        TemporalService temporalService = temporalServiceDao.getTemporalService(service, naturalArea);
        TemporalService2 temporalService2 = new TemporalService2();
        temporalService2.setBeginningDate(temporalService.getBeginningDate());
        temporalService2.setBeginningTime(temporalService.getBeginningTime());
        temporalService2.setEndDate(temporalService.getEndDate());
        temporalService2.setEndTime(temporalService.getEndTime());
        temporalService2.setNaturalArea(temporalService.getNaturalArea());
        temporalService2.setService(temporalService.getService());

        List<DaysOfWeek> diasMarcados = new ArrayList<DaysOfWeek>();
        for (String dia : temporalService.getOpeningDays().split(","))
            diasMarcados.add(DaysOfWeek.valueOf(dia));

        temporalService2.setDiasMarcados(diasMarcados);
        model.addAttribute("temporalService", temporalService2);
        return "temporalService/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("temporalService") TemporalService2 temporalService2,
                                      BindingResult bindingResult) {
        TemporalServiceValidator2 temporalServiceValidator2 = new TemporalServiceValidator2();
        temporalServiceValidator2.validate(temporalService2, bindingResult);
        if (bindingResult.hasErrors())
            return "temporalService/update";

        // Paso de lista de diasMarcados a openingDays de la bbdd
        List<DaysOfWeek> diasMarcados = temporalService2.getDiasMarcados();
        String openingDays = "";
        for(int i = 0; i < diasMarcados.size(); i++) {
            openingDays += diasMarcados.get(i);
            if (i < diasMarcados.size() - 1)
                openingDays += ",";
        }

        TemporalService temporalService = new TemporalService();
        temporalService.setService(temporalService2.getService());
        temporalService.setNaturalArea(temporalService2.getNaturalArea());
        temporalService.setEndTime(temporalService2.getEndTime());
        temporalService.setEndDate(temporalService2.getEndDate());
        temporalService.setBeginningTime(temporalService2.getBeginningTime());
        temporalService.setBeginningDate(temporalService2.getBeginningDate());
        temporalService.setOpeningDays(openingDays);

        temporalServiceDao.updateTemporalService(temporalService);
        String naturalAreaName = temporalService.getNaturalArea();
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{service}/{naturalArea}")
    public String processDelete(Model model, @PathVariable String service, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/temporalService/delete/" + service + "/" + naturalArea);
            return "/inicio/login";
        }
        TemporalService temporalService = temporalServiceDao.getTemporalService(service, naturalArea);
        String naturalAreaName = temporalService.getNaturalArea();
        temporalServiceDao.deleteTemporalService(service, naturalArea);
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }

    // información de un servicio temporal
    @RequestMapping(value="/get/{serviceName}/{naturalArea}")
    public String getTemporalServiceNaturalArea(Model model, HttpSession session, @PathVariable("serviceName") String serviceName,
                                                @PathVariable("naturalArea") String naturalArea){
        TemporalService temporalService = temporalServiceDao.getTemporalService(serviceName, naturalArea);
        model.addAttribute("temporalService", temporalService);
        model.addAttribute("service", serviceDao.getService(serviceName));
        List<String> listaDias = new ArrayList<>();
        for(String dia : Arrays.asList(temporalService.getOpeningDays().split(",")))
            listaDias.add(DaysOfWeek.valueOf(dia).getDescripcion());
        model.addAttribute("dias", listaDias);
        return "/temporalService/get";
    }
}
