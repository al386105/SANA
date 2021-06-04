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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/occupancy")
public class OccupancyController {
    private OccupationService occupationService;
    private MunicipalityDao municipalityDao;
    private NaturalAreaDao naturalAreaDao;

    @Autowired
    public void setNaturalAreaDao(NaturalAreaDao naturalAreaDao){
        this.naturalAreaDao = naturalAreaDao;
    }

    @Autowired
    public void setMunicipalityDao(MunicipalityDao municipalityDao){
        this.municipalityDao = municipalityDao;
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


    // Histórico de ocupación de municipios para responsable del medio ambiente
    @RequestMapping(value="/environmentalManager" ,method=RequestMethod.GET)
    public String getOccupancyEnvironmentalManager(Model model, HttpSession session,
                                                   @RequestParam(value="municipality",required=false) String municipality){
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/occupancy/environmentalManager");
            return "redirect:/inicio/login";
        }
        List<NaturalArea> naturalAreas = naturalAreaDao.getRestrictedNaturalAreas();
        List<Municipality> municipalities = municipalityDao.getMunicipalities();

        // filtrar por municipio
        if (municipality != null && !municipality.equals("todos"))
            naturalAreas.removeIf(naturalArea -> !naturalArea.getMunicipality().equals(municipality));

        model.addAttribute("municipalities", municipalities);
        model.addAttribute("naturalAreas", naturalAreas);
        model.addAttribute("occupancyDataOfNaturalAreas",
                occupationService.getOccupancyDataOfNaturalAreas(naturalAreas));
        return "occupancy/environmentalManager";
    }


    // Historico de ocupacion de áreas naturales para el gestor municipal
    @RequestMapping(value="/municipalManager", method=RequestMethod.GET)
    public String getOccupancyMunicipalManager(Model model, HttpSession session) {
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

    // metodo para anyadir al modelo los datos de anyos posibles
    @ModelAttribute("yearList")
    public List<Integer> yearList() {
        List<Integer> yearList = new ArrayList<>();
        for(int i = LocalDate.now().getYear(); i >= 2018; i--)
            yearList.add(i);
        return yearList;
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
                                occupancyFormData.getYear(), occupancyFormData.getMonth().getNum()));
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
