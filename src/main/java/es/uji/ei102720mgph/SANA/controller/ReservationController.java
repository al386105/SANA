package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.NaturalAreaDao;
import es.uji.ei102720mgph.SANA.dao.ReservationDao;
import es.uji.ei102720mgph.SANA.dao.TimeSlotDao;
import es.uji.ei102720mgph.SANA.dao.ZoneDao;
import es.uji.ei102720mgph.SANA.enums.ReservationState;
import es.uji.ei102720mgph.SANA.model.*;
import es.uji.ei102720mgph.SANA.services.ReservationService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Formatter;
import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    @Value("${upload.file.directory}")
    private String uploadDirectory;

    private ReservationDao reservationDao;
    private NaturalAreaDao naturalAreaDao;
    private ReservationService reservationService;
    private TimeSlotDao timeSlotDao;
    private ZoneDao zoneDao;

    @Autowired
    public void setReservationDao(ReservationDao reservationDao) {
        this.reservationDao=reservationDao;
    }

    @Autowired
    public void setNaturalAreaDao(NaturalAreaDao naturalAreaDao) {
        this.naturalAreaDao=naturalAreaDao;
    }

    @Autowired
    public void setZoneDao(ZoneDao zoneDao) {
        this.zoneDao = zoneDao;
    }

    @Autowired
    public void setReservationService(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @Autowired
    public void setTimeSlotDao(TimeSlotDao timeSlotDao){
        this.timeSlotDao = timeSlotDao;
    }

    // Operació llistar
    @RequestMapping("/listManagers")
    public String listManagersReservations(Model model) {
        model.addAttribute("reservations", reservationDao.getReservations());
        return "reservation/listManagers";
    }

    // Operació crear
    @RequestMapping(value="/add/{naturalArea}")
    public String addReservation(Model model, @PathVariable String naturalArea, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null)  {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/reservation/add/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("reservation", new NuevaReserva());
        model.addAttribute("naturalArea", naturalArea);
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlotNaturalAreaActuales(naturalArea));
        LocalDate[] fechas = new LocalDate[3];
        fechas[0] = LocalDate.now(); //PARA ESE MISMO DIA SE PUEDE RESERVAR ?? TODO
        // Sip, hasta 1 hora antes del inicio de la franja horaria
        fechas[1] = LocalDate.now().plusDays(1);
        fechas[2] = LocalDate.now().plusDays(2);
        model.addAttribute("fechas", fechas);
        return "reservation/add";
    }

    @RequestMapping(value="/add2/{naturalArea}")
    public String addReservation2(@ModelAttribute("reservation") NuevaReserva reservation, @PathVariable String naturalArea, Model model, HttpSession session) {
        model.addAttribute("reservation", reservation);
        model.addAttribute("naturalArea", naturalArea);
        if (reservation.getReservationDate().isEqual(LocalDate.now())) {
            TimeSlot timeSlot = timeSlotDao.getTimeSlot(reservation.getTimeSlotId());
            System.out.println(timeSlot.getBeginningTime());
            System.out.println(timeSlot.getBeginningTime().minusHours(1));
            System.out.println(LocalTime.now());
            System.out.println();
            if (timeSlot.getBeginningTime().minusHours(1).isAfter(LocalTime.now())) {
                model.addAttribute("puedeReservar", true);
            } else {
                model.addAttribute("puedeReservar", false);
            }
        }
        else {
            model.addAttribute("puedeReservar", true);
        }
        List<Zone> zonas = zoneDao.getZoneDisponibles(reservation.getReservationDate(), reservation.getTimeSlotId(), reservation.getNumberOfPeople(), (String) model.getAttribute("naturalArea"));
        model.addAttribute("zones", zonas);
        return "reservation/add2";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("reservation") NuevaReserva reservation,
                                   BindingResult bindingResult, HttpSession session) {
        // TODO VALIDADOR RESERVA
        /*
        ReservationValidator reservationValidator = new ReservationValidator();
        reservationValidator.validate(reservation, bindingResult);

        if (bindingResult.hasErrors()) {
            return "reservation/add2"; //tornem al formulari per a que el corregisca
        }
         */

        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        reservation.setCitizenEmail(citizen.getEmail());
        String zonas = reservation.getZoneid();
        String[] partes = zonas.split(",");

        for (String zon : partes) {
            reservation.setZoneid(zon);

            int numRes = reservationDao.addReservationPocosValores(reservation);
            reservationDao.addReservationOfZone(numRes, reservation.getZoneid());
            String naturalArea = naturalAreaDao.getNaturalAreaOfZone(reservation.getZoneid()).getName(); //TODO dara problemas
            String timeSlotId = reservation.getTimeSlotId();
            TimeSlot timeSlot = timeSlotDao.getTimeSlot(timeSlotId);


            // Generar QR
            Formatter fmt = new Formatter();
            QRCode qr = new QRCode();
            File f = new File("qr" + fmt.format("%07d", numRes) + ".png");
            String text = "Reserva por " + reservation.getCitizenEmail() + " en " + naturalArea + ", de fecha " + reservation.getReservationDate()
                    + ", de " + timeSlot.getBeginningTime() + " a " + timeSlot.getEndTime() + ", para " + reservation.getNumberOfPeople() + " personas.";

            try {
                qr.generateQR(f, text, 300, 300);
                byte[] bytes = Files.readAllBytes(f.toPath());
                Path path = Paths.get(uploadDirectory + "qrCodes/" + f.getName());
                Files.write(path, bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return "redirect:/inicio/registrado/reservas"; //redirigim a la lista per a veure el reservation afegit, post/redirect/get
    }

    // todo SI ACTUALIZAMOS, GENERAR DE NUEVO EL QR PQ SU INFO CAMBIA
    // Operació actualitzar
    @RequestMapping(value="/update/{reservationNumber}", method = RequestMethod.GET)
    public String editReservation(Model model, @PathVariable int reservationNumber) {
        model.addAttribute("reservation", reservationDao.getReservation(reservationNumber));
        return "reservation/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("reservation") Reservation reservation,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "reservation/update";
        reservationDao.updateReservation(reservation);
        return "redirect:list";
    }

    // Operació actualitzar per municipal manager
    @RequestMapping(value="/updateManagers/{reservationNumber}", method = RequestMethod.GET)
    public String editReservationManagers(Model model, @PathVariable int reservationNumber) {
        model.addAttribute("reservation", reservationDao.getReservation(reservationNumber));
        return "reservation/updateManagers";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/updateManagers", method = RequestMethod.POST)
    public String processUpdateSubmitManagers(@ModelAttribute("reservation") Reservation reservation,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "reservation/update";
        reservationDao.updateReservation(reservation);
        return "redirect:/reservation/listManagers";
    }

    // Operació cancel·lar pel municipal manager
    @RequestMapping(value="/cancelManagers/{reservationNumber}")
    public String processCancelManagers(@PathVariable int reservationNumber) {
        Reservation reservation = reservationDao.getReservation(reservationNumber);
        reservation.setState(ReservationState.cancelledMunicipalManager);
        return "reservation/cancelManagers";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/cancelManagers", method = RequestMethod.POST)
    public String processCancelManagersSubmit(@ModelAttribute("reservation") Reservation reservation,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "reservation/update";
        reservation.setCancellationDate(LocalDate.now());
        reservationDao.updateReservation(reservation);

        // GENERAR DE NUEVO QR (mirar lo que he puesto en un QR de cancelado por gestor municipal

        return "redirect:/reservation/listManagers/";
    }

    // Operació esborrar
    /*@RequestMapping(value="/delete/{reservationNumber}")
    public String processDelete(@PathVariable int reservationNumber) {
        reservationDao.deleteReservation(reservationNumber);
        return "redirect:../list";
    }*/
}