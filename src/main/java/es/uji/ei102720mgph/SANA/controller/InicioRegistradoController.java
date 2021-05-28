package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.model.*;
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
import java.util.Formatter;
import java.util.List;

@Controller
@RequestMapping("/inicio/registrado")
public class InicioRegistradoController {
    @Value("${upload.file.directory}")
    private String uploadDirectory;

    private NaturalAreaDao naturalAreaDao;
    private TimeSlotDao timeSlotDao;
    private ReservaDatosDao reservaDatosDao;
    private ReservationDao reservationDao;

    @Autowired
    public void setNaturalAreaDao(NaturalAreaDao naturalAreaDao){
        this.naturalAreaDao = naturalAreaDao;
    }

    @Autowired
    public void setTimeSlotDao(TimeSlotDao timeSlotDao){
        this.timeSlotDao = timeSlotDao;
    }

    @Autowired
    public void setReservationDao(ReservationDao reservationDao){
        this.reservationDao = reservationDao;
    }

    @Autowired
    public void setReservaDatosDao(ReservaDatosDao reservaDatosDao){
        this.reservaDatosDao = reservaDatosDao;
    }

    @RequestMapping("/")
    public String redirigirRegistrado(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicioRegistrado/areasNaturales");
            return "redirect:/inicio/login";
        }
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        model.addAttribute("citizen", citizen);
        return "inicioRegistrado/areasNaturales";
    }

    @RequestMapping("/reservas")
    public String redirigirRegistradoReservas(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicio/registrado/reservas");
            return "redirect:/inicio/login";
        }
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        List<ReservaDatos> listaReservas = reservaDatosDao.getReservasEmail(citizen.getEmail());
        for (int i = 0; i < listaReservas.size(); i++) {
            Reservation res = reservationDao.getReservation(listaReservas.get(i).getReservationNumber());
            int max = reservationDao.getMaximumCapacity(res.getReservationNumber());
            model.addAttribute("maxPersonas" + listaReservas.get(i).getReservationNumber(), max);
        }
        model.addAttribute("motivo", new MotivoCancelancion());
        model.addAttribute("personas", new PersonasReserva());
        model.addAttribute("reservas", listaReservas);
        return "inicioRegistrado/reservas";
    }

    @RequestMapping("/cancelarReserva/{id}")
    public String cancelarReserva(@ModelAttribute("motivo") MotivoCancelancion motivo, @PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicio/registrado/reservas");
            return "redirect:/inicio/login";
        }
        String mot = motivo.getMot();
        mot = mot.substring(0, mot.length()-1);
        reservaDatosDao.cancelaReservaPorCiudadano(id, mot);

        // Actualizar QR con la cancelacion
        Reservation reservation = reservationDao.getReservation(Integer.parseInt(id));
        Formatter fmt = new Formatter();
        QRCode qr = new QRCode();
        File f = new File("qr" + fmt.format("%07d", id) + ".png");
        String text = "Reserva cancelada por " + reservation.getCitizenEmail() + " de fecha " + reservation.getReservationDate() + ".";
        try {
            qr.generateQR(f, text, 300, 300);
            byte[] bytes = Files.readAllBytes(f.toPath());
            Path path = Paths.get(uploadDirectory + "qrCodes/" + f.getName());
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inicio/registrado/reservas";
    }

    @RequestMapping("/editarReserva/{id}")
    public String editarReserva(@ModelAttribute("personas") PersonasReserva personas, @PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicio/registrado/reservas");
            return "redirect:/inicio/login";
        }

        int pers = personas.getNum();
        System.out.println(pers);
        //reservaDatosDao.cancelaReservaPorCiudadano(id, personas);


        // Actualizar QR con los nuevos datos de la reserva
        /*Reservation reservation = reservationDao.getReservation(Integer.parseInt(id));
        String naturalArea = naturalAreaDao.getNaturalAreaOfZone(reservation.getZoneid()).getName(); // TODO da problemas con varias zonas
        String timeSlotId = reservation.getTimeSlotId();
        TimeSlot timeSlot = timeSlotDao.getTimeSlot(timeSlotId);
        Formatter fmt = new Formatter();
        QRCode qr = new QRCode();
        File f = new File("qr" + fmt.format("%07d", id) + ".png");
        String text = "Reserva por " + reservation.getCitizenEmail() + " en " + naturalArea + ", de fecha " + reservation.getReservationDate()
                + ", de " + timeSlot.getBeginningTime() + " a " + timeSlot.getEndTime() + ", para " + reservation.getNumberOfPeople() + " personas.";
        try {
            qr.generateQR(f, text, 300, 300);
            byte[] bytes = Files.readAllBytes(f.toPath());
            Path path = Paths.get(uploadDirectory + "qrCodes/" + f.getName());
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return "redirect:/inicio/registrado/reservas";
    }

    @RequestMapping("/reservasTodas")
    public String redirigirRegistradoReservasTodas(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicio/registrado/reservasTodas");
            return "redirect:/inicio/login";
        }
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        List<ReservaDatos> listaReservas = reservaDatosDao.getReservasTodasEmail(citizen.getEmail());
        model.addAttribute("reservas", listaReservas);
        return "inicioRegistrado/reservasTodas";
    }

    @RequestMapping("/perfil")
    public String redirigirRegistradoPerfil(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicio/registrado/perfil");
            return "redirect:/inicio/login";
        }
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");

        model.addAttribute("citizen", citizen);
        return "inicioRegistrado/perfil";
    }

    @RequestMapping("/editarPerfil")
    public String redirigirRegistradoEditarPerfil(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicioRegistrado/editarPerfil");
            return "redirect:/inicio/login";
        }
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        model.addAttribute("citizen", citizen);
        return "inicioRegistrado/editarPerfil";
    }

    @RequestMapping(value="/editarPerfil", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("citizen") RegisteredCitizen registeredCitizen,
                                      BindingResult bindingResult, Model model, HttpSession session) {
        PerfilValidator perfilValidator = new PerfilValidator();
        perfilValidator.validate(registeredCitizen, bindingResult);
        if (bindingResult.hasErrors())
            return "inicio/registrado/editarPerfil";
        model.addAttribute("citizen", registeredCitizen);
        session.setAttribute("registeredCitizen", registeredCitizen);
        return "redirect:perfil";
    }
}
