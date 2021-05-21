package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.enums.TypeOfUser;
import es.uji.ei102720mgph.SANA.model.*;
import es.uji.ei102720mgph.SANA.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Formatter;
import java.util.List;
import java.util.Properties;

class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return UserLogin.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {
        UserLogin user = (UserLogin) obj;

        if (user.getEmail().trim().equals(""))
            errors.rejectValue("email", "obligatorio", "Necesario introducir el email");

        if (user.getPassword().trim().equals(""))
            errors.rejectValue("password", "obligatorio", "Necesario introducir la contraseña");
    }
}


@Controller
@RequestMapping("/")
public class AuxiliarController {
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




    // TODO este método igual debería ir en el controller de reservation
    @RequestMapping("inicio/registrado/reservas")
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

    @RequestMapping("inicio/registrado/cancelarReserva/{id}")
    public String cancelarReserva(@ModelAttribute("motivo") MotivoCancelancion motivo, @PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicio/registrado/reservas");
            return "redirect:/inicio/login";
        }
        String mot = motivo.getMot();
        mot = mot.substring(0, mot.length()-1);
        reservaDatosDao.cancelaReservaPorCiudadano(id, mot);

        Reservation reservation = reservationDao.getReservation(Integer.parseInt(id));
        // Actualizar QR con la cancelacion
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

    @RequestMapping("inicio/registrado/editarReserva/{id}")
    public String editarReserva(@ModelAttribute("personas") PersonasReserva personas, @PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicio/registrado/reservas");
            return "redirect:/inicio/login";
        }

        int pers = personas.getNum();
        System.out.println(pers);
        //reservaDatosDao.cancelaReservaPorCiudadano(id, personas);

        return "redirect:/inicio/registrado/reservas";
    }

    @RequestMapping("inicio/registrado/reservasTodas")
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







}
