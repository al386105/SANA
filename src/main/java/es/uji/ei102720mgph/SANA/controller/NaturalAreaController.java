package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.enums.Orientation;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.enums.TypeOfArea;
import es.uji.ei102720mgph.SANA.model.Municipality;
import es.uji.ei102720mgph.SANA.model.NaturalArea;
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
@RequestMapping("/naturalArea")
public class NaturalAreaController {
    private NaturalAreaDao naturalAreaDao;
    private ZoneDao zoneDao;
    private CommentDao commentDao;
    private MunicipalityDao municipalityDao;
    private PictureDao pictureDao;
    private ReservationDao reservationDao;
    private TimeSlotDao timeSlotDao;
    private ServiceDateDao serviceDateDao;
    private TemporalServiceDao temporalServiceDao;

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
        model.addAttribute("timeslots", timeSlotDao.getTimeSlotNaturalArea(naturalArea));
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
        model.addAttribute("timeslots", timeSlotDao.getTimeSlotNaturalArea(naturalArea));
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
        model.addAttribute("naturalAreas", naturalAreaDao.getNaturalAreas());
        return "naturalArea/list";
    }

    @RequestMapping(value="/listManagers")
    public String listNaturalAreasManagers(Model model){
        model.addAttribute("naturalAreas", naturalAreaDao.getNaturalAreas());
        return "naturalArea/listManagers";
    }

    @RequestMapping(value="/listEnvironmental")
    public String listNaturalAreasEnvironmental(Model model){
        model.addAttribute("naturalAreas", naturalAreaDao.getNaturalAreas());
        return "naturalArea/listEnvironmental";
    }

    @RequestMapping(value="/occupancy")
    public String occupancyNaturalAreas(Model model){
        model.addAttribute("naturalAreas", naturalAreaDao.getNaturalAreas());
        return "naturalArea/occupancy";
    }

    @RequestMapping(value="/add")
    public String addNaturalArea(Model model) {
        model.addAttribute("naturalArea", new NaturalArea());
        model.addAttribute("typeOfAccessList", TypeOfAccess.values());
        model.addAttribute("typeOfAreaList", TypeOfArea.values());
        model.addAttribute("orientationList", Orientation.values());
        List<Municipality> municipalityList = municipalityDao.getMunicipalities();
        List<String> namesMunicipalities = municipalityList.stream()          // sols els seus noms
                .map(Municipality::getName)
                .collect(Collectors.toList());
        model.addAttribute("municipalityList", namesMunicipalities);
        return "naturalArea/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("naturalArea") NaturalArea naturalArea,
                                   BindingResult bindingResult) {
        NaturalAreaValidator naturalAreaValidator = new NaturalAreaValidator();
        naturalAreaValidator.validate(naturalArea, bindingResult);

        System.out.println("nombre " + naturalArea.getName());
        if (bindingResult.hasErrors())
            return "naturalArea/add"; //tornem al formulari per a que el corregisca
        naturalAreaDao.addNaturalArea(naturalArea); //usem el dao per a inserir el naturalArea
        return "redirect:/naturalArea/getManagers/" + naturalArea.getName();
    }

    // Update
    @RequestMapping(value="/update/{naturalArea}", method=RequestMethod.GET)
    public String editNaturalArea(Model model, @PathVariable String naturalArea) {
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("typeOfAccessList", TypeOfAccess.values());
        model.addAttribute("typeOfAreaList", TypeOfArea.values());
        model.addAttribute("orientationList", Orientation.values());
        List<Municipality> municipalityList = municipalityDao.getMunicipalities();
        List<String> namesMunicipalities = municipalityList.stream()          // sols els seus noms
                .map(Municipality::getName)
                .collect(Collectors.toList());
        model.addAttribute("municipalityList", namesMunicipalities);
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
        return "redirect:/naturalArea/getManagers/" + naturalArea.getName();
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{naturalArea}")
    public String processDelete(@PathVariable String naturalArea) {
        naturalAreaDao.deleteNaturalArea(naturalArea);
        return "redirect:/naturalArea/listManagers";
    }
}
