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
    private ReservaDatosDao reservaDatosDao;
    private TimeSlotDao timeSlotDao;
    private ZoneDao zoneDao;
    private EmailDao emailDao;

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
    public void setReservaDatosDao(ReservaDatosDao reservaDatosDao){
        this.reservaDatosDao = reservaDatosDao;
    }

    @Autowired
    public void setTimeSlotDao(TimeSlotDao timeSlotDao){
        this.timeSlotDao = timeSlotDao;
    }

    @Autowired
    public void setEmailDao(EmailDao emailDao) {
        this.emailDao = emailDao;
    }


    // Operació crear
    @RequestMapping(value="/add/{naturalArea}")
    public String addReservation(Model model, @PathVariable String naturalArea, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null)  {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/reservation/add/" + naturalArea);
            return "redirect:/inicio/login";
        }
        if(session.getAttribute("nextUrl") != null)
            session.removeAttribute("nextUrl");
        model.addAttribute("reservation", new NuevaReserva());
        model.addAttribute("naturalArea", naturalArea);
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlotNaturalAreaActuales(naturalArea));
        LocalDate[] fechas = new LocalDate[3];
        fechas[0] = LocalDate.now();
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
            String naturalArea = naturalAreaDao.getNaturalAreaOfZone(partes[0]).getName();
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

            // Enviar mail con la reserva
            String destinatario = reservation.getCitizenEmail();
            String asunto = "Reserva completada";
            String cuerpo = "Reserva realizada correctamente el día " + reservation.getReservationDate() +
            " para " + reservation.getNumberOfPeople()  + " personas. \n\nUn cordial saludo del equipo de SANA.";
            Email email = HomeController.enviarMail(destinatario, asunto, cuerpo);
            emailDao.addEmail(email);
        }
        return "redirect:/reservation/reservas"; //redirigim a la lista per a veure el reservation afegit
    }


    @RequestMapping("/cancelarReserva/{id}")
    public String cancelarReserva(@ModelAttribute("motivo") MotivoCancelancion motivo, @PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/reservation/reservas");
            return "redirect:/inicio/login";
        }
        String mot = motivo.getMot();
        reservaDatosDao.cancelaReservaPorCiudadano(id, mot);

        // Actualizar QR con la cancelacion
        Reservation reservation = reservationDao.getReservation(Integer.parseInt(id));
        Formatter fmt = new Formatter();
        QRCode qr = new QRCode();
        File f = new File("qr" + fmt.format("%07d", Integer.parseInt(id)) + ".png");
        String text = "Reserva cancelada por " + reservation.getCitizenEmail() + " de fecha " + reservation.getReservationDate() + ".";
        try {
            qr.generateQR(f, text, 300, 300);
            byte[] bytes = Files.readAllBytes(f.toPath());
            Path path = Paths.get(uploadDirectory + "qrCodes/" + f.getName());
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Enviar mail con la cancelacion de la reserva
        String destinatario = reservation.getCitizenEmail();
        String asunto = "Reserva cancelada";
        String cuerpo = "Ha cancelado su reserva correctamente del día " + reservation.getReservationDate() +
                " para " + reservation.getNumberOfPeople()  + " personas. \n\nUn cordial saludo del equipo de SANA.";
        Email email = HomeController.enviarMail(destinatario, asunto, cuerpo);
        emailDao.addEmail(email);
        return "redirect:/reservation/reservas";
    }

    //TODO
    @RequestMapping("/editarReserva/{id}")
    public String editarReserva(@ModelAttribute("personas") PersonasReserva personas, @PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/reservation/reservas");
            return "redirect:/inicio/login";
        }

        int pers = personas.getNum();
        System.out.println(pers);
        //reservaDatosDao.cancelaReservaPorCiudadano(id, personas);

        // Actualizar QR con los nuevos datos de la reserva
        Reservation reservation = reservationDao.getReservation(Integer.parseInt(id));
        String timeSlotId = reservation.getTimeSlotId();
        TimeSlot timeSlot = timeSlotDao.getTimeSlot(timeSlotId);
        Formatter fmt = new Formatter();
        QRCode qr = new QRCode();
        File f = new File("qr" + fmt.format("%07d", Integer.parseInt(id)) + ".png");
        String text = "Reserva por " + reservation.getCitizenEmail() + ", de fecha " + reservation.getReservationDate()
                + ", de " + timeSlot.getBeginningTime() + " a " + timeSlot.getEndTime() + ", para " + reservation.getNumberOfPeople() + " personas.";
        try {
            qr.generateQR(f, text, 300, 300);
            byte[] bytes = Files.readAllBytes(f.toPath());
            Path path = Paths.get(uploadDirectory + "qrCodes/" + f.getName());
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Enviar mail con la modificacion de la reserva
        String destinatario = reservation.getCitizenEmail();
        String asunto = "Reserva actualizada";
        String cuerpo = "Ha actualizado correctamente los datos de la reserva del día " + reservation.getReservationDate() +
                " para " + reservation.getNumberOfPeople()  + " personas. \n\nUn cordial saludo del equipo de SANA.";
        Email email = HomeController.enviarMail(destinatario, asunto, cuerpo);
        emailDao.addEmail(email);
        return "redirect:/reservation/reservas";
    }


    @RequestMapping("/cancelarReservaMunicipal/{naturalArea}/{id}")
    public String cancelarReservaMunicipal(@ModelAttribute("motivo") MotivoCancelancion motivo, @PathVariable String naturalArea, @PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("municipalManager") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/reservation/reservasArea/" + naturalArea);
            return "redirect:/inicio/login";
        }
        String mot = motivo.getMot();
        reservaDatosDao.cancelaReservaPorMunicipal(id, mot);

        // Actualizar QR con la cancelacion
        Reservation reservation = reservationDao.getReservation(Integer.parseInt(id));
        Formatter fmt = new Formatter();
        QRCode qr = new QRCode();
        File f = new File("qr" + fmt.format("%07d", Integer.parseInt(id)) + ".png");
        String text = "Reserva cancelada por gestor municipal a " + reservation.getCitizenEmail() + " de fecha " + reservation.getReservationDate() + ".";
        try {
            qr.generateQR(f, text, 300, 300);
            byte[] bytes = Files.readAllBytes(f.toPath());
            Path path = Paths.get(uploadDirectory + "qrCodes/" + f.getName());
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Enviar mail con la cancelacion de la reserva
        String destinatario = reservation.getCitizenEmail();
        String asunto = "Reserva cancelada";
        String cuerpo = "Su reserva del día " + reservation.getReservationDate() +
                " para " + reservation.getNumberOfPeople()  + " personas ha sido cancelada por el gestor municipal." +
                "\n\nUn cordial saludo del equipo de SANA.";
        Email email = HomeController.enviarMail(destinatario, asunto, cuerpo);
        emailDao.addEmail(email);
        return "redirect:/reservation/reservasArea/" + naturalArea;
    }

    @RequestMapping("/reservas")
    public String redirigirRegistradoReservas(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/reservation/reservas");
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
        return "/reservation/reservas";
    }

    @RequestMapping("/reservasTodas")
    public String redirigirRegistradoReservasTodas(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/reservation/reservasTodas");
            return "redirect:/inicio/login";
        }
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        List<ReservaDatos> listaReservas = reservaDatosDao.getReservasTodasEmail(citizen.getEmail());
        model.addAttribute("reservas", listaReservas);
        return "/reservation/reservasTodas";
    }

    @RequestMapping(value="/reservasArea/{naturalArea}")
    public String reservasArea(Model model, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/reservation/reservasArea/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("reservas", reservaDatosDao.getReservasNaturalArea(naturalArea));
        model.addAttribute("motivo", new MotivoCancelancion());
        model.addAttribute("naturalArea", naturalArea);
        return "reservation/reservasArea";
    }

    @RequestMapping(value="/reservasTodasArea/{naturalArea}")
    public String todasReservasnaturalArea(Model model, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/reservation/reservasTodasArea/" + naturalArea);
            return "redirect:/inicio/login";
        }

        List<ReservaDatosMunicipal> listaReservas = reservaDatosDao.getReservasTodasNaturalArea(naturalArea);
        model.addAttribute("reservas", listaReservas);
        model.addAttribute("naturalArea", naturalArea);
        return "reservation/reservasTodasArea";
    }
}