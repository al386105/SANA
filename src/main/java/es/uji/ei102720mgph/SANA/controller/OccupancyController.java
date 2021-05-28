package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.model.*;
import es.uji.ei102720mgph.SANA.services.OccupationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/occupancy")
public class OccupancyController {
    private OccupationService occupationService;
    private NaturalAreaDao naturalAreaDao;

    @Autowired
    public void setNaturalAreaDao(NaturalAreaDao naturalAreaDao){
        this.naturalAreaDao = naturalAreaDao;
    }

    @Autowired
    public void setOccupationService(OccupationService occupationService){
        this.occupationService = occupationService;
    }

    // Vista de paneles de información para ciudadanos registrados o no registrados
    @RequestMapping(value="/panel")
    public String getInfoPaneles(Model model, HttpSession session){
        // si es null es que no esta registrado
        model.addAttribute("registered", session.getAttribute("registeredCitizen"));
        List<NaturalArea> naturalAreas = naturalAreaDao.getNaturalAreas();
        model.addAttribute("occupancyDataOfNaturalAreas",
                occupationService.getOccupancyDataOfNaturalAreas(naturalAreas));
        return "occupancy/panel";
    }


    // Historico de ocupacion de áreas naturales para el gestor municipal
    @RequestMapping(value="/municipalManager", method=RequestMethod.GET)
    public String getOccupancy(Model model, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/occupancy/municipalManager");
            return "redirect:/inicio/login";
        }
        OccupancyFormData occupancyFormData = new OccupancyFormData();
        model.addAttribute("occupancyFormData", occupancyFormData);
        List<NaturalArea> naturalAreas = naturalAreaDao.getRestrictedNaturalAreas();
        model.addAttribute("naturalAreas", naturalAreas);
        model.addAttribute("occupancyDataOfNaturalAreas",
                occupationService.getOccupancyDataOfNaturalAreas(naturalAreas));
        return "occupancy/municipalManager";
    }

    // Formulario para obtener el gráfico de ocupación para el municipal manager
    @RequestMapping(value="/plotForm")
    public String occupancyPlotFormGet(Model model, HttpSession session,
                                   @ModelAttribute("occupancyFormData") OccupancyFormData occupancyFormData){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/occupancy/plotForm");
            return "redirect:/inicio/login";
        }
        return "occupancy/plotForm";
    }

    @RequestMapping(value="/plotForm", method=RequestMethod.POST)
    public String occupancyPlotSubmit(Model model, HttpSession session,
                                    @ModelAttribute("occupancyFormData") OccupancyFormData occupancyFormData,
                                      BindingResult bindingResult){

        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/occupancy/plotForm");
            return "redirect:/inicio/login";
        }

        OccupancyFormValidator occupancyFormValidator = new OccupancyFormValidator();
        occupancyFormValidator.validate(occupancyFormData, bindingResult);
        if (bindingResult.hasErrors())
            return "occupancy/plotForm";


        switch (occupancyFormData.getTypeOfPeriod().getDescripcion()) {
            case "Por dia":
                model.addAttribute("plot",
                        occupationService.getOccupancyPlotByDay(occupancyFormData.getNaturalArea(),
                                occupancyFormData.getDay()));
                break;
            case "Por mes":
                model.addAttribute("plot",
                        occupationService.getOccupancyPlotByMonth(occupancyFormData.getNaturalArea(),
                                occupancyFormData.getYear(), occupancyFormData.getMonth()));
                break;
            case "Por año":
                model.addAttribute("plot",
                        occupationService.getOccupancyPlotByYear(occupancyFormData.getNaturalArea(),
                                occupancyFormData.getYear()));
                break;
        }

        return "occupancy/plotDisplay";
    }
}
