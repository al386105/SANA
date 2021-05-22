package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.dao.TemporalServiceDao;
import es.uji.ei102720mgph.SANA.enums.DaysOfWeek;
import es.uji.ei102720mgph.SANA.model.*;
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
            return "redirect:/inicio/login";
        }
        TemporalService temporalService = new TemporalService();
        temporalService.setNaturalArea(naturalArea);
        session.setAttribute("section", "#temporalServices");
        model.addAttribute("temporalService", temporalService);
        return "temporalService/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("temporalService") TemporalService temporalService,
                                   Model model, BindingResult bindingResult) {
        TemporalServiceValidator temporalServiceValidator = new TemporalServiceValidator();
        temporalServiceValidator.validate(temporalService, bindingResult);
        if(model.getAttribute("selector") != null)
            model.addAttribute("selector", null);

        if (bindingResult.hasErrors())
            return "temporalService/add";
        try {
            temporalServiceDao.addTemporalService(temporalService);
        } catch (DataIntegrityViolationException e) {
            // selector no seleccionado
            model.addAttribute("selector", "noSeleccionado");
            return "temporalService/add";
        }
        return "redirect:/naturalArea/getManagers/" + temporalService.getNaturalArea();
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editTemporalService(Model model, @PathVariable String id, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/temporalService/update/" + id);
            return "redirect:/inicio/login";
        }
        TemporalService temporalService = temporalServiceDao.getTemporalService(id);
        TemporalService2 temporalService2 = new TemporalService2();
        temporalService2.setId(temporalService.getId());
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
        session.setAttribute("section", "#temporalServices");
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
        temporalService.setId(temporalService2.getId());
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

    // información de un servicio temporal
    @RequestMapping(value="/get/{id}")
    public String getTemporalServiceNaturalArea(Model model, @PathVariable String id, HttpSession session){

        // Pasar qué tipo de usuario es para mostrar unos botones u otros en la misma vista
        if(session.getAttribute("municipalManager") != null)
            model.addAttribute("typeUser", "manager");
        else if(session.getAttribute("registeredCitizen") != null)
            model.addAttribute("typeUser", "registered");
        else if(session.getAttribute("environmentalManager") != null)
            model.addAttribute("typeUser", "environmental");

        session.setAttribute("section", "#temporalServices");
        TemporalService temporalService = temporalServiceDao.getTemporalService(id);
        model.addAttribute("temporalService", temporalService);
        model.addAttribute("service", serviceDao.getService(temporalService.getService()));
        List<String> listaDias = new ArrayList<>();
        for(String dia : Arrays.asList(temporalService.getOpeningDays().split(",")))
            listaDias.add(DaysOfWeek.valueOf(dia).getDescripcion());
        model.addAttribute("dias", listaDias);
        return "/temporalService/get";
    }
}
