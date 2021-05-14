package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.enums.Orientation;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.enums.TypeOfArea;
import es.uji.ei102720mgph.SANA.model.Municipality;
import es.uji.ei102720mgph.SANA.model.NaturalArea;
import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
import es.uji.ei102720mgph.SANA.services.NaturalAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/naturalArea")
public class NaturalAreaController {
    private NaturalAreaDao naturalAreaDao;
    private NaturalAreaService naturalAreaService;
    private ZoneDao zoneDao;
    private CommentDao commentDao;
    private MunicipalityDao municipalityDao;
    private PictureDao pictureDao;
    private ReservationDao reservationDao;
    private TimeSlotDao timeSlotDao;
    private ServiceDateDao serviceDateDao;
    private TemporalServiceDao temporalServiceDao;

    private final int pageLength = 5;


    @Autowired
    public void setNaturalAreaService(NaturalAreaService naturalAreaService){
        this.naturalAreaService = naturalAreaService;
    }

    @Autowired
    public void setNaturalAreaDao(NaturalAreaDao naturalAreaDao){ this.naturalAreaDao = naturalAreaDao; }

    @Autowired
    public void setZoneDao(ZoneDao zoneDao){ this.zoneDao = zoneDao; }

    @Autowired
    public void setCommentDao(CommentDao commentDao) { this.commentDao = commentDao; }

    @Autowired
    public void setMunicipalityDao(MunicipalityDao municipalityDao) { this.municipalityDao = municipalityDao; }

    @Autowired
    public void setPictureDao(PictureDao pictureDao) { this.pictureDao = pictureDao; }

    @Autowired
    public void setReservationDao(ReservationDao reservationDao) { this.reservationDao = reservationDao; }

    @Autowired
    public void setTimeSlotDao(TimeSlotDao timeSlotDao) { this.timeSlotDao = timeSlotDao; }

    @Autowired
    public void setServiceDateDao(ServiceDateDao serviceDateDao) { this.serviceDateDao = serviceDateDao; }

    @Autowired
    public void setTemporalServiceDao(TemporalServiceDao temporalServiceDao) { this.temporalServiceDao = temporalServiceDao; }

    @RequestMapping(value="/get/{naturalArea}")
    public String getNaturalArea(Model model, @PathVariable("naturalArea") String naturalArea){
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        model.addAttribute("comments", commentDao.getCommentsOfNaturalArea(naturalArea));
        model.addAttribute("pictures", pictureDao.getPicturesOfNaturalArea(naturalArea));
        model.addAttribute("serviceDates", serviceDateDao.getServiceDatesOfNaturalArea(naturalArea));
        model.addAttribute("temporalServices", temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea));
        return "/naturalArea/get";
    }

    @RequestMapping(value="/getManagers/{naturalArea}")
    public String getNaturalAreaManagers(Model model, @PathVariable("naturalArea") String naturalArea){
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        model.addAttribute("comments", commentDao.getCommentsOfNaturalArea(naturalArea));
        model.addAttribute("pictures", pictureDao.getPicturesOfNaturalArea(naturalArea));
        model.addAttribute("serviceDates", serviceDateDao.getServiceDatesOfNaturalArea(naturalArea));
        model.addAttribute("temporalServices", temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea));
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlotNaturalArea(naturalArea));
        return "/naturalArea/getManagers";
    }

    @RequestMapping(value="/getEnvironmental/{naturalArea}")
    public String getNaturalAreaEnvironmental(Model model, @PathVariable("naturalArea") String naturalArea){
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        model.addAttribute("comments", commentDao.getCommentsOfNaturalArea(naturalArea));
        model.addAttribute("pictures", pictureDao.getPicturesOfNaturalArea(naturalArea));
        model.addAttribute("serviceDates", serviceDateDao.getServiceDatesOfNaturalArea(naturalArea));
        model.addAttribute("temporalServices", temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea));
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlotNaturalArea(naturalArea));
        return "/naturalArea/getEnvironmental";
    }

    // TODO en proceso
    @RequestMapping(value="/getReservations/{naturalArea}")
    public String getReservationsNaturalArea(Model model, @PathVariable("naturalArea") String naturalArea){
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("reservations", reservationDao.getReservationsOfNaturalArea(naturalArea));
        return "/naturalArea/getReservations";
    }

    @RequestMapping(value="/list")
    public String listNaturalAreas(Model model){
        model.addAttribute("naturalAreas", naturalAreaService.getNaturalAreasWithImage());
        return "naturalArea/list";
    }


    @RequestMapping(value="/pagedlist")
    public String listNaturalAreasPaged(Model model, HttpSession session,
                                        @RequestParam("page") Optional<Integer> page){
        // Paso 1: Crear la lista paginada de naturalAreas
        List<NaturalArea> naturalAreas = naturalAreaDao.getNaturalAreas();
        Collections.sort(naturalAreas);
        // las áreas naturales cerradas no pueden ser vistas por los ciudadanos
        naturalAreas.removeIf(naturalArea -> naturalArea.getTypeOfAccess().getDescripcion().equals("Cerrado"));

        List<String> pathPictures = naturalAreaService.getImageOfNaturalAreas(naturalAreas);
        ArrayList<ArrayList<NaturalArea>> naturalAreasPaged = new ArrayList<>();
        ArrayList<ArrayList<String>> pathPicturesPaged = new ArrayList<>();
        int ini=0;
        int fin=pageLength-1;
        while (fin<naturalAreas.size()) {
            pathPicturesPaged.add(new ArrayList<>(pathPictures.subList(ini, fin)));
            naturalAreasPaged.add(new ArrayList<>(naturalAreas.subList(ini, fin)));
            ini+=pageLength;
            fin+=pageLength;
        }
        naturalAreasPaged.add(new ArrayList<NaturalArea>(naturalAreas.subList(ini, naturalAreas.size())));
        pathPicturesPaged.add(new ArrayList<String>(pathPictures.subList(ini, naturalAreas.size())));
        model.addAttribute("naturalAreasPaged", naturalAreasPaged);
        model.addAttribute("pathPicturesPaged", pathPicturesPaged);

        // Paso 2: Crear la lista de numeros de pagina
        int totalPages = naturalAreasPaged.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        // Paso 3: selectedPage: usar parametro opcional page, o en su defecto, 1
        int currentPage = page.orElse(0);
        model.addAttribute("selectedPage", currentPage);

        if (session.getAttribute("registeredCitizen") == null) return "inicio/sana";
        return "inicioRegistrado/areasNaturales";
    }

    @RequestMapping(value="/listEnvironmental")
    public String listNaturalAreasEnvironmental(Model model){
        model.addAttribute("naturalAreas", naturalAreaDao.getNaturalAreas());
        return "naturalArea/listEnvironmental";
    }



    @RequestMapping(value="/listManagers")
    public String listNaturalAreasManagers(Model model, @RequestParam("page") Optional<Integer> page){
        // Paso 1: Crear la lista paginada de naturalAreas
        List<NaturalArea> naturalAreas = naturalAreaDao.getNaturalAreas();
        Collections.sort(naturalAreas);
        ArrayList<ArrayList<NaturalArea>> naturalAreasPaged = new ArrayList<>();
        int ini=0;
        int fin=pageLength-1;
        while (fin<naturalAreas.size()) {
            naturalAreasPaged.add(new ArrayList<>(naturalAreas.subList(ini, fin)));
            ini+=pageLength;
            fin+=pageLength;
        }
        naturalAreasPaged.add(new ArrayList<NaturalArea>(naturalAreas.subList(ini, naturalAreas.size())));
        model.addAttribute("naturalAreasPaged", naturalAreasPaged);

        // Paso 2: Crear la lista de numeros de pagina
        int totalPages = naturalAreasPaged.size();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        // Paso 3: selectedPage: usar parametro opcional page, o en su defecto, 1
        int currentPage = page.orElse(0);
        model.addAttribute("selectedPage", currentPage);
        return "naturalArea/listManagers";
        // TODO if (session.getAttribute("registeredCitizen") == null) return "inicio/sana";
        //return "inicioRegistrado/areasNaturales";
        //return "redirect:/inicioRegistrado/areasNaturales";
    }



    @RequestMapping(value="/occupancy")
    public String occupancyNaturalAreas(Model model){
        model.addAttribute("naturalAreas", naturalAreaDao.getNaturalAreas());
        return "naturalArea/occupancy";
    }

    // metodo para anyadir al modelo los datos del selector de municipio
    @ModelAttribute("municipalityList")
    public List<String> municipalityList() {
        List<Municipality> municipalityList = municipalityDao.getMunicipalities();
        List<String> namesMunicipalities = municipalityList.stream()          // sols els seus noms
                .map(Municipality::getName)
                .collect(Collectors.toList());
        return namesMunicipalities;
    }

    @RequestMapping(value="/add")
    public String addNaturalArea(Model model) {
        model.addAttribute("naturalArea", new NaturalArea());
        return "naturalArea/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("naturalArea") NaturalArea naturalArea,
                                   BindingResult bindingResult) {
        NaturalAreaValidator naturalAreaValidator = new NaturalAreaValidator();
        naturalAreaValidator.validate(naturalArea, bindingResult);

        if (bindingResult.hasErrors())
            return "naturalArea/add"; //tornem al formulari per a que el corregisca
        naturalAreaDao.addNaturalArea(naturalArea); //usem el dao per a inserir el naturalArea

        // si es vol afegir un area restringida, redirigir a la vista per a continuar la seua creació
        if(naturalArea.getTypeOfAccess() == TypeOfAccess.restricted)
            return "naturalArea/addRestricted";
        return "redirect:/naturalArea/getManagers/" + naturalArea.getName();
    }

    // Update
    @RequestMapping(value="/update/{naturalArea}", method=RequestMethod.GET)
    public String editNaturalArea(Model model, @PathVariable String naturalArea) {
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        return "naturalArea/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("naturalArea") NaturalArea naturalArea,
                                      BindingResult bindingResult) {
        NaturalAreaValidator naturalAreaValidator = new NaturalAreaValidator();
        naturalAreaValidator.validate(naturalArea, bindingResult);

        if (bindingResult.hasErrors())
            return "naturalArea/update";
        naturalAreaDao.updateNaturalArea(naturalArea);
        // si es vol actualitzar un area restringida, redirigir a la vista per a continuar la seua creació
        if(naturalArea.getTypeOfAccess() == TypeOfAccess.restricted)
            return "naturalArea/updateRestricted";
        // por si he pasado de un area restringida a una no restringida
        naturalArea.setRestrictionTimePeriod(null);
        return "redirect:/naturalArea/getManagers/" + naturalArea.getName();
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/updateRestricted", method=RequestMethod.POST)
    public String processUpdateRestrictedSubmit(@ModelAttribute("naturalArea") NaturalArea naturalArea,
                                                BindingResult bindingResult) {
        NaturalAreaValidator naturalAreaValidator = new NaturalAreaValidator();
        naturalAreaValidator.validate(naturalArea, bindingResult);

        if (bindingResult.hasErrors())
            return "naturalArea/updateRestricted";
        naturalAreaDao.updateNaturalArea(naturalArea);
        return "redirect:/naturalArea/getManagers/" + naturalArea.getName();
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{naturalArea}")
    public String processDelete(@PathVariable String naturalArea) {
        naturalAreaDao.deleteNaturalArea(naturalArea);
        return "redirect:/naturalArea/listManagers";
    }
}
