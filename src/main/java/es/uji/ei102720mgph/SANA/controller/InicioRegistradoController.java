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

    private ReservaDatosDao reservaDatosDao;
    private ReservationDao reservationDao;

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
        for (ReservaDatos listaReserva : listaReservas) {
            Reservation res = reservationDao.getReservation(listaReserva.getReservationNumber());
            int max = reservationDao.getMaximumCapacity(res.getReservationNumber());
            model.addAttribute("maxPersonas" + listaReserva.getReservationNumber(), max);
        }
        model.addAttribute("motivo", new MotivoCancelancion());
        model.addAttribute("personas", new PersonasReserva());
        model.addAttribute("reservas", listaReservas);
        return "inicioRegistrado/reservas";
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
