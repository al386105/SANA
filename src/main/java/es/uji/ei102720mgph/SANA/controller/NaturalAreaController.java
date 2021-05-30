package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.enums.TypeOfArea;
import es.uji.ei102720mgph.SANA.model.*;
import es.uji.ei102720mgph.SANA.services.NaturalAreaService;
import es.uji.ei102720mgph.SANA.services.OccupationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
    private TimeSlotDao timeSlotDao;
    private ServiceDateDao serviceDateDao;
    private TemporalServiceDao temporalServiceDao;
    private ServiceDao serviceDao;
    private ReservaDatosDao reservaDatosDao;

    private final int pageLength = 5;

    @Autowired
    public void setOccupationService(OccupationService occupationService){
        this.occupationService = occupationService;
    }

    @Autowired
    public void setReservaDatosDao(ReservaDatosDao reservaDatosDao) {
        this.reservaDatosDao = reservaDatosDao;
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

        // Mostrar sólo los servicios fijos operativos (fecha de inicio anterior a fecha actual)
        List<ServiceDate> serviceDates = serviceDateDao.getServiceDatesOfNaturalAreaOperativos(naturalArea);
        serviceDates.removeIf(service -> service.getBeginningDate().isAfter(LocalDate.now()));

        // No nos interesa la fecha de inicio, asiq le pasamos directamente el servicio para la tabla y punto
        List<Service> servicios = new ArrayList<>();
        for(ServiceDate serviceDate : serviceDates)
            servicios.add(serviceDao.getService(serviceDate.getService()));
        model.addAttribute("serviceDates", servicios);

        // Mostrar sólo los servicios temporales operativos (fecha actual entre inicio y fin)
        List<TemporalService> temporalServices = temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea);
        temporalServices.removeIf(service -> service.getBeginningDate().isAfter(LocalDate.now()) || service.getEndDate().isBefore(LocalDate.now()));
        model.addAttribute("temporalServices", temporalServices);

        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/naturalArea/get/" + naturalArea + section;
        }
        return "/naturalArea/get";
    }

    @RequestMapping(value="/getForManagers/{naturalArea}")
    public String getNaturalAreaForManagers(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/getForManagers/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("comments", commentDao.getCommentsOfNaturalArea(naturalArea));
        model.addAttribute("pictures", pictureDao.getPicturesOfNaturalArea(naturalArea));
        serviceDateLista(model, naturalArea);

        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/naturalArea/getForManagers/" + naturalArea + section;
        }
        return "/naturalArea/getForManagers";
    }

    @RequestMapping(value="/getForManagersServices/{naturalArea}")
    public String getServicesForManagers(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/getForManagersServices/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        serviceDateLista(model, naturalArea);
        model.addAttribute("temporalServices", temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea));
        model.addAttribute("serviceDatesFaltan", serviceDao.getServiceDatesNotInNaturalArea(naturalArea));
        model.addAttribute("service", new Service());

        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/naturalArea/getForManagersServices/" + naturalArea + section;
        }
        return "/naturalArea/getForManagersServices";
    }

    @RequestMapping(value="/getForManagersTimeSlots/{naturalArea}")
    public String getTimeSlotsForManagers(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/getForManagersTimeSlots/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        serviceDateLista(model, naturalArea);
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlotNaturalArea(naturalArea));

        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/naturalArea/getForManagersTimeSlots/" + naturalArea + section;
        }
        return "/naturalArea/getForManagersTimeSlots";
    }

    @RequestMapping(value="/getForManagersZones/{naturalArea}")
    public String getZonesForManagers(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/getForManagersZones/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        serviceDateLista(model, naturalArea);

        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/naturalArea/getForManagersZones/" + naturalArea + section;
        }
        return "/naturalArea/getForManagersZones";
    }

    @RequestMapping(value="/getForManagers/{naturalArea}")
    public String getNaturalAreaManagers(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/getForManagers/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        model.addAttribute("comments", commentDao.getCommentsOfNaturalArea(naturalArea));
        model.addAttribute("pictures", pictureDao.getPicturesOfNaturalArea(naturalArea));
        serviceDateLista(model, naturalArea);
        model.addAttribute("temporalServices", temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea));
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlotNaturalArea(naturalArea));
        model.addAttribute("serviceDatesFaltan", serviceDao.getServiceDatesNotInNaturalArea(naturalArea));
        model.addAttribute("service", new Service());

        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/naturalArea/getForManagers/" + naturalArea + section;
        }
        return "/naturalArea/getForManagers";
    }

    @RequestMapping(value="/getForEnvironmental/{naturalArea}")
    public String getNaturalAreaEnvironmental(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/getForEnvironmental/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        model.addAttribute("comments", commentDao.getCommentsOfNaturalArea(naturalArea));
        model.addAttribute("pictures", pictureDao.getPicturesOfNaturalArea(naturalArea));
        model.addAttribute("temporalServices", temporalServiceDao.getTemporalServicesOfNaturalArea(naturalArea));
        serviceDateLista(model, naturalArea);
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlotNaturalArea(naturalArea));

        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            // Eliminar atribut de la sessio
            session.removeAttribute("section");
            return "redirect:/naturalArea/getForEnvironmental/" + naturalArea + section;
        }
        return "/naturalArea/getForEnvironmental";
    }

    private void serviceDateLista(Model model, String naturalArea) {
        //transformar los serviceDates a ServiceDateList para que tengan más atributos (los de las tablas)
        List<ServiceDate> serviceDates = serviceDateDao.getServiceDatesOfNaturalAreaOperativos(naturalArea);
        List<ServiceDateList> services = new ArrayList<>();
        for(ServiceDate serviceDate : serviceDates) {
            Service servicio = serviceDao.getService(serviceDate.getService());
            ServiceDateList s = new ServiceDateList();
            s.setNameOfService(serviceDate.getService());
            s.setBeginningDate(serviceDate.getBeginningDate());
            s.setDescription(servicio.getDescription());
            s.setHiringPlace(servicio.getHiringPlace());
            s.setId(serviceDate.getId());
            services.add(s);
        }
        model.addAttribute("serviceDates", services);
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

    @RequestMapping(value="/pagedlist")
    public String listNaturalAreasPaged(Model model, HttpSession session, @RequestParam(value="patron",required=false) String patron,
                                        @RequestParam(value="typeOfArea",required=false) String typeOfArea,
                                        @RequestParam(value="typeOfAccess",required=false) String typeOfAccess,
                                        @RequestParam(value="municipality",required=false) String municipality,
                                        @RequestParam("page") Optional<Integer> page){
        quitarAtributoSeccion(session);
        // Paso 1: Crear la lista paginada de naturalAreas
        List<NaturalArea> naturalAreas;
        if (patron != null) {
            // Aplicar filtros
            naturalAreas = new ArrayList<>();
            naturalAreas = filtrar(naturalAreas, patron, typeOfArea, typeOfAccess, municipality);
        } else
            naturalAreas = naturalAreaDao.getNaturalAreas();

        // las áreas naturales cerradas no pueden ser vistas por los ciudadanos
        naturalAreas.removeIf(naturalArea -> naturalArea.getTypeOfAccess().getDescripcion().equals("Cerrado"));
        // Ordenar por nombre
        Collections.sort(naturalAreas);
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

        if (session.getAttribute("registeredCitizen") == null)
            return "inicio/areasNaturales";
        return "inicioRegistrado/areasNaturales";
    }

    private List<NaturalArea> filtrar(List<NaturalArea> naturalAreas, String patron, String typeOfArea, String typeOfAccess, String municipality) {
        if (patron != null && !patron.equals(""))
            naturalAreas = naturalAreaDao.getNaturalAreaSearch(patron);
        else
            naturalAreas = naturalAreaDao.getNaturalAreas();

        // filtro por tipo de área
        if (typeOfArea != null && !typeOfArea.equals("todas"))
            naturalAreas.removeIf(naturalArea -> naturalArea.getTypeOfArea() != TypeOfArea.valueOf(typeOfArea));

        // filtro por tipo de acceso
        if (typeOfAccess != null && !typeOfAccess.equals("todas"))
            naturalAreas.removeIf(naturalArea -> naturalArea.getTypeOfAccess() != TypeOfAccess.valueOf(typeOfAccess));

        // filtro por municipio
        if (municipality != null && !municipality.equals("todos"))
            naturalAreas.removeIf(naturalArea -> !naturalArea.getMunicipality().equals(municipality));

        return naturalAreas;
    }

    @RequestMapping(value="/listForEnvironmental")
    public String listNaturalAreasEnvironmental(HttpSession session, Model model, @RequestParam(value="patron",required=false) String patron,
                                                @RequestParam(value="typeOfArea",required=false) String typeOfArea,
                                                @RequestParam(value="typeOfAccess",required=false) String typeOfAccess,
                                                @RequestParam(value="municipality",required=false) String municipality,
                                                @RequestParam("page") Optional<Integer> page){
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/listForEnvironmental");
            return "redirect:/inicio/login";
        }
        quitarAtributoSeccion(session);
        paginacionSinFotos(model, patron, typeOfArea, typeOfAccess, municipality, page);
        return "naturalArea/listForEnvironmental";
    }

    @RequestMapping(value="/listForManagers")
    public String listNaturalAreasManagers(Model model, @RequestParam(value="patron",required=false) String patron,
                                           @RequestParam(value="typeOfArea",required=false) String typeOfArea,
                                           @RequestParam(value="typeOfAccess",required=false) String typeOfAccess,
                                           @RequestParam(value="municipality",required=false) String municipality,
                                           @RequestParam("page") Optional<Integer> page, HttpSession session){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/listForManagers");
            return "redirect:/inicio/login";
        }
        paginacionSinFotos(model, patron, typeOfArea, typeOfAccess, municipality, page);
        quitarAtributoSeccion(session);
        return "naturalArea/listForManagers";
    }

    private void paginacionSinFotos(Model model, String patron, String typeOfArea, String typeOfAccess,
                                    String municipality, @RequestParam("page") Optional<Integer> page) {
        // Paso 1: Crear la lista paginada de naturalAreas
        List<NaturalArea> naturalAreas;
        if (patron != null) {
            // Aplicar filtros
            naturalAreas = new ArrayList<>();
            naturalAreas = filtrar(naturalAreas, patron, typeOfArea, typeOfAccess, municipality);
        } else
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
    public String processAddSubmit(Model model, @ModelAttribute("naturalArea") NaturalAreaForm naturalAreaForm,
                                   BindingResult bindingResult) {
        NaturalAreaValidator naturalAreaValidator = new NaturalAreaValidator();
        naturalAreaValidator.validate(naturalAreaForm, bindingResult);

        if(model.getAttribute("claveRepetida") != null)
            model.addAttribute("claveRepetida", null);
        if(model.getAttribute("selector") != null)
            model.addAttribute("selector", null);

        if (bindingResult.hasErrors())
            return "naturalArea/add"; //tornem al formulari per a que el corregisca

        NaturalArea naturalArea = pasoANaturalArea(naturalAreaForm);
        // si es vol afegir un area restringida, redirigir a la vista per a continuar la seua creació
        if(naturalArea.getTypeOfAccess() == TypeOfAccess.restricted)
            return "naturalArea/addRestricted";
        try {
            naturalAreaDao.addNaturalArea(naturalArea);
        } catch (DataIntegrityViolationException e) {
            // nombre del área ya existente
            if(naturalAreaDao.getNaturalArea(naturalArea.getName()) != null)
                model.addAttribute("claveRepetida", "repetida");
            // selector no seleccionado
            if(naturalArea.getMunicipality().equals("No seleccionado"))
                model.addAttribute("selector", "noSeleccionado");
            return "naturalArea/add";
        }
        return "redirect:/naturalArea/getForManagers/" + naturalArea.getName();
    }

    // addRestricted
    @RequestMapping(value="/addRestricted", method= RequestMethod.POST)
    public String processAddRestrictedSubmit(Model model, @ModelAttribute("naturalArea") NaturalAreaForm naturalAreaForm,
                                             BindingResult bindingResult, HttpSession session) {
        NaturalAreaValidadorRestricted naturalAreaValidator = new NaturalAreaValidadorRestricted();
        naturalAreaValidator.validate(naturalAreaForm, bindingResult);

        if(model.getAttribute("claveRepetida") != null)
            model.addAttribute("claveRepetida", null);
        if(model.getAttribute("selector") != null)
            model.addAttribute("selector", null);

        if (bindingResult.hasErrors())
            return "naturalArea/addRestricted";
        NaturalArea naturalArea = naturalAreaDao.getNaturalArea(naturalAreaForm.getName());
        try {
            naturalAreaDao.addNaturalArea(pasoANaturalArea(naturalAreaForm));
        } catch (DataIntegrityViolationException e) {
            // nombre del área ya existente
            if(naturalAreaDao.getNaturalArea(naturalArea.getName()) != null)
                model.addAttribute("claveRepetida", "repetida");
            // selector no seleccionado
            if(naturalArea.getMunicipality().equals("No seleccionado"))
                model.addAttribute("selector", "noSeleccionado");
            return "naturalArea/add";
        }
        return "redirect:/naturalArea/getForManagers/" + naturalAreaForm.getName();
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
        return "redirect:/naturalArea/getForManagers/" + naturalArea.getName();
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
        return "redirect:/naturalArea/getForManagers/" + naturalAreaForm.getName();
    }


    // Vista de paneles de información para ciudadanos registrados o no registrados
    @RequestMapping(value="/getInfo")
    public String getInfoPaneles(Model model, HttpSession session){
        // si es null es que no esta registrado
        model.addAttribute("registered", session.getAttribute("registeredCitizen"));
        List<NaturalArea> naturalAreas = naturalAreaDao.getNaturalAreas();
        model.addAttribute("occupancyDataOfNaturalAreas",
                occupationService.getOccupancyDataOfNaturalAreas(naturalAreas));
        return "naturalArea/getInfo";
    }


    // Historico de ocupacion de áreas naturales para el gestor municipal
    @RequestMapping(value="/occupancy", method=RequestMethod.GET)
    public String getOccupancy(Model model, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/occupancy");
            return "redirect:/inicio/login";
        }
        OccupancyFormData occupancyFormData = new OccupancyFormData();
        model.addAttribute("occupancyFormData", occupancyFormData);
        List<NaturalArea> naturalAreas = naturalAreaDao.getRestrictedNaturalAreas();
        model.addAttribute("naturalAreas", naturalAreas);
        model.addAttribute("occupancyDataOfNaturalAreas",
                occupationService.getOccupancyDataOfNaturalAreas(naturalAreas));
        return "naturalArea/occupancy";
    }

    // Vista de paneles de información para ciudadanos registrados o no registrados
    @RequestMapping(value="/occupancyPlotForm")
    public String occupancyPlotFormGet(Model model, HttpSession session,
                                   @ModelAttribute("occupancyFormData") OccupancyFormData occupancyFormData){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/occupancyPlotForm");
            return "redirect:/inicio/login";
        }
        return "naturalArea/occupancyPlotForm";
    }

    @RequestMapping(value="/occupancyPlotForm", method=RequestMethod.POST)
    public String occupancyPlotSubmit(Model model, HttpSession session,
                                    @ModelAttribute("occupancyFormData") OccupancyFormData occupancyFormData,
                                      BindingResult bindingResult){

        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/occupancyPlotForm");
            return "redirect:/inicio/login";
        }

        OccupancyFormValidator occupancyFormValidator = new OccupancyFormValidator();
        occupancyFormValidator.validate(occupancyFormData, bindingResult);
        if (bindingResult.hasErrors())
            return "naturalArea/occupancyPlotForm";


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

        return "naturalArea/occupancyPlot";
    }

    private void quitarAtributoSeccion(HttpSession session) {
        if(session.getAttribute("section") != null)
            session.removeAttribute("section");
    }

    @RequestMapping(value="/getReservations/{naturalArea}")
    public String getReservations(Model model, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/getReservations/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("reservas", reservaDatosDao.getReservasNaturalArea(naturalArea));
        model.addAttribute("motivo", new MotivoCancelancion());
        model.addAttribute("naturalArea", naturalArea);
        return "naturalArea/reservas";
    }

    @RequestMapping("/cancelarReservaMunicipal/{naturalArea}/{id}")
    public String cancelarReserva(@ModelAttribute("motivo") MotivoCancelancion motivo, @PathVariable String naturalArea, @PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("municipalManager") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/getReservations/" + naturalArea);
            return "redirect:/inicio/login";
        }
        String mot = motivo.getMot();
        mot = mot.substring(0, mot.length()-1);
        reservaDatosDao.cancelaReservaPorMunicipal(id, mot);

        return "redirect:/naturalArea/getReservations/" + naturalArea;
    }

    @RequestMapping(value="/reservasTodas/{naturalArea}")
    public String todasReservasnaturalArea(Model model, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/naturalArea/getReservations/" + naturalArea);
            return "redirect:/inicio/login";
        }

        List<ReservaDatos> listaReservas = reservaDatosDao.getReservasTodasNaturalArea(naturalArea);
        model.addAttribute("reservas", listaReservas);
        model.addAttribute("naturalArea", naturalArea);
        return "naturalArea/reservasTodas";
    }
}
