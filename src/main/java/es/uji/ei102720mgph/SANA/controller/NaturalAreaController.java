package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.model.*;
import es.uji.ei102720mgph.SANA.services.NaturalAreaService;
import es.uji.ei102720mgph.SANA.services.OccupationService;
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
    private OccupationService occupationService;
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
    private ServiceDao serviceDao;

    private final int pageLength = 5;

    @Autowired
    public void setOccupationService(OccupationService occupationService){
        this.occupationService = occupationService;
    }

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

    @Autowired
    public void setTemporalServiceDao(ServiceDao serviceDao) { this.serviceDao = serviceDao; }

    @RequestMapping(value="/get/{naturalArea}")
    public String getNaturalArea(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session) {
        if(session.getAttribute("registeredCitizen") != null)
            model.addAttribute("typeUser", "registered");
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        model.addAttribute("comments", commentDao.getCommentsOfNaturalArea(naturalArea));
        model.addAttribute("pictures", pictureDao.getPicturesOfNaturalArea(naturalArea));
        model.addAttribute("serviceDates", serviceDateDao.getServiceDatesOfNaturalArea(naturalArea));
        model.addAttribute("temporalServices", temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea));

        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/naturalArea/get/" + naturalArea + section;
        }
        return "/naturalArea/get";
    }

    @RequestMapping(value="/getManagers/{naturalArea}")
    public String getNaturalAreaManagers(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/getManagers/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        model.addAttribute("comments", commentDao.getCommentsOfNaturalArea(naturalArea));
        model.addAttribute("pictures", pictureDao.getPicturesOfNaturalArea(naturalArea));
        model.addAttribute("serviceDates", serviceDateDao.getServiceDatesOfNaturalArea(naturalArea));
        model.addAttribute("temporalServices", temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea));
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlotNaturalArea(naturalArea));
        model.addAttribute("serviceDatesFaltan", serviceDao.getServiceDatesNotInNaturalArea(naturalArea));

        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/naturalArea/getManagers/" + naturalArea + section;
        }
        return "/naturalArea/getManagers";
    }

    @RequestMapping(value="/getEnvironmental/{naturalArea}")
    public String getNaturalAreaEnvironmental(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/getEnvironmental/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        model.addAttribute("comments", commentDao.getCommentsOfNaturalArea(naturalArea));
        model.addAttribute("pictures", pictureDao.getPicturesOfNaturalArea(naturalArea));
        model.addAttribute("serviceDates", serviceDateDao.getServiceDatesOfNaturalArea(naturalArea));
        model.addAttribute("temporalServices", temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea));
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlotNaturalArea(naturalArea));

        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/naturalArea/getEnvironmental/" + naturalArea + section;
        }
        return "/naturalArea/getEnvironmental";
    }

    // TODO en proceso ¿de quien es esto?
    @RequestMapping(value="/getReservations/{naturalArea}")
    public String getReservationsNaturalArea(Model model, @PathVariable("naturalArea") String naturalArea){
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("reservations", reservationDao.getReservationsOfNaturalArea(naturalArea));
        return "/naturalArea/getReservations";
    }

    @RequestMapping(value="/list")
    public String listNaturalAreas(Model model, HttpSession session){
        model.addAttribute("naturalAreas", naturalAreaService.getNaturalAreasWithImage());
        quitarAtributoSeccion(session);
        return "naturalArea/list";
    }

    @RequestMapping(value="/pagedlist")
    public String listNaturalAreasPaged(Model model, HttpSession session, @RequestParam(value="patron",required=false) String patron,
                                        @RequestParam("page") Optional<Integer> page){
        quitarAtributoSeccion(session);
        // Paso 1: Crear la lista paginada de naturalAreas
        List<NaturalArea> naturalAreas;
        if (patron != null)
            naturalAreas = naturalAreaDao.getNaturalAreaSearch(patron);
        else
            naturalAreas = naturalAreaDao.getNaturalAreas();
        Collections.sort(naturalAreas);

        // las áreas naturales cerradas no pueden ser vistas por los ciudadanos
        naturalAreas.removeIf(naturalArea -> naturalArea.getTypeOfAccess().getDescripcion().equals("Cerrado"));

        List<String> pathPictures = naturalAreaService.getImageOfNaturalAreas(naturalAreas);
        ArrayList<ArrayList<NaturalArea>> naturalAreasPaged = new ArrayList<>();
        ArrayList<ArrayList<String>> pathPicturesPaged = new ArrayList<>();
        int ini=0;
        int fin=pageLength;
        while (fin<naturalAreas.size()) {
            pathPicturesPaged.add(new ArrayList<>(pathPictures.subList(ini, fin)));
            naturalAreasPaged.add(new ArrayList<>(naturalAreas.subList(ini, fin)));
            ini+=pageLength;
            fin+=pageLength;
        }
        naturalAreasPaged.add(new ArrayList<>(naturalAreas.subList(ini, naturalAreas.size())));
        pathPicturesPaged.add(new ArrayList<>(pathPictures.subList(ini, pathPictures.size())));
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
    public String listNaturalAreasEnvironmental(HttpSession session, Model model, @RequestParam(value="patron",required=false) String patron, @RequestParam("page") Optional<Integer> page){
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/listEnvironmental");
            return "redirect:/inicio/login";
        }
        quitarAtributoSeccion(session);
        paginacionSinFotos(model, patron, page);
        return "naturalArea/listEnvironmental";
    }

    @RequestMapping(value="/listManagers")
    public String listNaturalAreasManagers(Model model, @RequestParam(value="patron",required=false) String patron, @RequestParam("page") Optional<Integer> page, HttpSession session){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/listManagers");
            return "redirect:/inicio/login";
        }
        paginacionSinFotos(model, patron, page);
        quitarAtributoSeccion(session);
        return "naturalArea/listManagers";
    }

    private void paginacionSinFotos(Model model, String patron, @RequestParam("page") Optional<Integer> page) {
        // Paso 1: Crear la lista paginada de naturalAreas
        List<NaturalArea> naturalAreas;
        if (patron != null)
            naturalAreas = naturalAreaDao.getNaturalAreaSearch(patron);
        else
            naturalAreas = naturalAreaDao.getNaturalAreas();
        Collections.sort(naturalAreas);
        ArrayList<ArrayList<NaturalArea>> naturalAreasPaged = new ArrayList<>();
        int ini=0;
        int fin=pageLength;
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
    public String addNaturalArea(Model model, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/add");
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", new NaturalAreaForm());
        return "naturalArea/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("naturalArea") NaturalAreaForm naturalAreaForm,
                                   BindingResult bindingResult) {
        NaturalAreaValidator naturalAreaValidator = new NaturalAreaValidator();
        naturalAreaValidator.validate(naturalAreaForm, bindingResult);

        if (bindingResult.hasErrors())
            return "naturalArea/add"; //tornem al formulari per a que el corregisca

        NaturalArea naturalArea = pasoANaturalArea(naturalAreaForm);
        // si es vol afegir un area restringida, redirigir a la vista per a continuar la seua creació
        if(naturalArea.getTypeOfAccess() == TypeOfAccess.restricted)
            return "naturalArea/addRestricted";
        naturalAreaDao.addNaturalArea(naturalArea); //usem el dao per a inserir el naturalArea
        return "redirect:/naturalArea/getManagers/" + naturalArea.getName();
    }

    // addRestricted
    @RequestMapping(value="/addRestricted", method= RequestMethod.POST)
    public String processAddRestrictedSubmit(@ModelAttribute("naturalArea") NaturalAreaForm naturalAreaForm,
                                             BindingResult bindingResult) {
        NaturalAreaValidadorRestricted naturalAreaValidator = new NaturalAreaValidadorRestricted();
        naturalAreaValidator.validate(naturalAreaForm, bindingResult);

        if (bindingResult.hasErrors())
            return "naturalArea/addRestricted";

        naturalAreaDao.addNaturalArea(pasoANaturalArea(naturalAreaForm)); //usem el dao per a inserir el naturalArea
        return "redirect:/naturalArea/getManagers/" + naturalAreaForm.getName();
    }

    // Convierte de NaturalAreaForm a NaturalArea
    private NaturalArea pasoANaturalArea(NaturalAreaForm naturalAreaForm) {
        NaturalArea naturalArea = new NaturalArea();
        naturalArea.setName(naturalAreaForm.getName());
        naturalArea.setTypeOfAccess(naturalAreaForm.getTypeOfAccess());

        // transformar las coordenadas a un único atributo
        String coordenadas = naturalAreaForm.getLatitudGrados() + "°" + naturalAreaForm.getLatitudMin() + "′" +
                naturalAreaForm.getLatitudSeg() + "″" + naturalAreaForm.getLatitudLetra() + " " +
                naturalAreaForm.getLongitudGrados() + "°" + naturalAreaForm.getLongitudMin() + "′" +
                naturalAreaForm.getLongitudSeg() + "″" + naturalAreaForm.getLongitudLetra();
        naturalArea.setGeographicalLocation(coordenadas);

        naturalArea.setTypeOfArea(naturalAreaForm.getTypeOfArea());
        naturalArea.setLength(naturalAreaForm.getLength());
        naturalArea.setWidth(naturalAreaForm.getWidth());
        naturalArea.setPhysicalCharacteristics(naturalAreaForm.getPhysicalCharacteristics());
        naturalArea.setDescription(naturalAreaForm.getDescription());
        naturalArea.setOrientation(naturalAreaForm.getOrientation());
        naturalArea.setMunicipality(naturalAreaForm.getMunicipality());
        if(naturalAreaForm.getRestrictionTimePeriod() != null)
            naturalArea.setRestrictionTimePeriod(naturalAreaForm.getRestrictionTimePeriod());
        return naturalArea;
    }

    private NaturalAreaForm pasoDeNaturalAreaAForm(NaturalArea naturalArea) {
        NaturalAreaForm naturalAreaForm = new NaturalAreaForm();
        naturalAreaForm.setName(naturalArea.getName());
        naturalAreaForm.setTypeOfAccess(naturalArea.getTypeOfAccess());

        // dividir las coordenadas en los distintos campos
        String[] dosPartes = naturalArea.getGeographicalLocation().split(" ");
        String[] partesLatitud1 = dosPartes[0].split("′");
        String[] partesLatitud2 = partesLatitud1[0].split("°");
        String[] partesLatitud3 = partesLatitud1[1].split("″");
        naturalAreaForm.setLatitudGrados(Float.parseFloat(partesLatitud2[0]));
        naturalAreaForm.setLatitudMin(Float.parseFloat(partesLatitud2[1]));
        naturalAreaForm.setLatitudSeg(Float.parseFloat(partesLatitud3[0]));
        naturalAreaForm.setLatitudLetra(partesLatitud3[1]);

        String[] partesLongitud1 = dosPartes[1].split("′");
        String[] partesLongitud2 = partesLongitud1[0].split("°");
        String[] partesLongitud3 = partesLongitud1[1].split("″");
        naturalAreaForm.setLongitudGrados(Float.parseFloat(partesLongitud2[0]));
        naturalAreaForm.setLongitudMin(Float.parseFloat(partesLongitud2[1]));
        naturalAreaForm.setLongitudSeg(Float.parseFloat(partesLongitud3[0]));
        naturalAreaForm.setLongitudLetra(partesLongitud3[1]);

        naturalAreaForm.setTypeOfArea(naturalArea.getTypeOfArea());
        naturalAreaForm.setLength(naturalArea.getLength());
        naturalAreaForm.setWidth(naturalArea.getWidth());
        naturalAreaForm.setPhysicalCharacteristics(naturalArea.getPhysicalCharacteristics());
        naturalAreaForm.setDescription(naturalArea.getDescription());
        naturalAreaForm.setOrientation(naturalArea.getOrientation());
        naturalAreaForm.setMunicipality(naturalArea.getMunicipality());
        if(naturalArea.getRestrictionTimePeriod() != null)
            naturalAreaForm.setRestrictionTimePeriod(naturalArea.getRestrictionTimePeriod());
        return naturalAreaForm;
    }

    // Update
    @RequestMapping(value="/update/{naturalArea}", method=RequestMethod.GET)
    public String editNaturalArea(Model model, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/update/" + naturalArea);
            return "redirect:/inicio/login";
        }
        quitarAtributoSeccion(session);
        model.addAttribute("naturalArea", pasoDeNaturalAreaAForm(naturalAreaDao.getNaturalArea(naturalArea)));
        return "naturalArea/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method=RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("naturalArea") NaturalAreaForm naturalAreaForm,
                                      BindingResult bindingResult) {
        NaturalAreaValidator naturalAreaValidator = new NaturalAreaValidator();
        naturalAreaValidator.validate(naturalAreaForm, bindingResult);

        if (bindingResult.hasErrors())
            return "naturalArea/update";

        NaturalArea naturalArea = pasoANaturalArea(naturalAreaForm);
        // por si he pasado de un area restringida a una no restringida
        if(naturalArea.getTypeOfAccess() != TypeOfAccess.restricted) {
            naturalArea.setRestrictionTimePeriod(null);
        }
        // si es vol actualitzar un area restringida, redirigir a la vista per a continuar
        if(naturalArea.getTypeOfAccess() == TypeOfAccess.restricted)
            return "naturalArea/updateRestricted";

        naturalAreaDao.updateNaturalArea(naturalArea);
        return "redirect:/naturalArea/getManagers/" + naturalArea.getName();
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/updateRestricted", method=RequestMethod.POST)
    public String processUpdateRestrictedSubmit(@ModelAttribute("naturalArea") NaturalAreaForm naturalAreaForm,
                                                BindingResult bindingResult) {
        NaturalAreaValidadorRestricted naturalAreaValidator = new NaturalAreaValidadorRestricted();
        naturalAreaValidator.validate(naturalAreaForm, bindingResult);

        if (bindingResult.hasErrors())
            return "naturalArea/updateRestricted";
        naturalAreaDao.updateNaturalArea(pasoANaturalArea(naturalAreaForm));
        return "redirect:/naturalArea/getManagers/" + naturalAreaForm.getName();
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{naturalArea}")
    public String processDelete(Model model, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/delete/" + naturalArea);
            return "redirect:/inicio/login";
        }
        naturalAreaDao.deleteNaturalArea(naturalArea);
        return "redirect:/naturalArea/listManagers";
    }

    @RequestMapping(value="/occupancy", method=RequestMethod.GET)
    public String getOccupancyForm(Model model, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/occupancy");
            return "redirect:/inicio/login";
        }
        List<NaturalArea> naturalAreas = naturalAreaDao.getRestrictedNaturalAreas();
        model.addAttribute("occupancyDataOfNaturalAreas", occupationService.getOccupancyDataOfNaturalAreas(naturalAreas));
        return "naturalArea/occupancy";
    }

    // Vista de paneles de información para ciudadanos registrados
    @RequestMapping(value="/getInfo")
    public String getInfoPaneles(Model model, HttpSession session){
        // si es null es que no esta registrado
        model.addAttribute("registered", session.getAttribute("registeredCitizen"));
        return "/naturalArea/getInfo";
    }

    private void quitarAtributoSeccion(HttpSession session) {
        if(session.getAttribute("section") != null)
            session.removeAttribute("section");
    }
}
