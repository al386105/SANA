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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
        this.reservationDao = reservationDao;
    }

    @Autowired
    public void setNaturalAreaDao(NaturalAreaDao naturalAreaDao) {
        this.naturalAreaDao = naturalAreaDao;
    }

    @Autowired
    public void setZoneDao(ZoneDao zoneDao) {
        this.zoneDao = zoneDao;
    }

    @Autowired
    public void setReservaDatosDao(ReservaDatosDao reservaDatosDao) {
        this.reservaDatosDao = reservaDatosDao;
    }

    @Autowired
    public void setTimeSlotDao(TimeSlotDao timeSlotDao) {
        this.timeSlotDao = timeSlotDao;
    }

    @Autowired
    public void setEmailDao(EmailDao emailDao) {
        this.emailDao = emailDao;
    }


    // Operació crear
    @RequestMapping(value = "/add/{naturalArea}")
    public String addReservation(Model model, @PathVariable String naturalArea, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {
            });
            session.setAttribute("nextUrl", "/reservation/add/" + naturalArea);
            return "redirect:/inicio/login";
        }
        if (session.getAttribute("nextUrl") != null)
            session.removeAttribute("nextUrl");
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        model.addAttribute("citizenName", citizen.getName());
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

    @RequestMapping(value = "/add2/{naturalArea}")
    public String addReservation2(@ModelAttribute("reservation") NuevaReserva reservation,
                                  @PathVariable String naturalArea, Model model, HttpSession session) {
        model.addAttribute("reservation", reservation);
        model.addAttribute("naturalArea", naturalArea);
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        model.addAttribute("citizenName", citizen.getName());
        if (reservation.getReservationDate().isEqual(LocalDate.now())) {
            TimeSlot timeSlot = timeSlotDao.getTimeSlot(reservation.getTimeSlotId());
            if (timeSlot.getBeginningTime().minusHours(1).isAfter(LocalTime.now())) {
                model.addAttribute("puedeReservar", true);
            } else {
                model.addAttribute("puedeReservar", false);
            }
        } else {
            model.addAttribute("puedeReservar", true);
        }
        List<Zone> zonas = zoneDao.getZoneDisponibles(reservation.getReservationDate(), reservation.getTimeSlotId(), reservation.getNumberOfPeople(), (String) model.getAttribute("naturalArea"));
        model.addAttribute("zones", zonas);
        return "reservation/add2";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("reservation") NuevaReserva reservation,
                                   HttpSession session, Model model, BindingResult bindingResult) {
        ReservationValidator resVal = new ReservationValidator();
        resVal.validate(reservation, bindingResult);
        if (bindingResult.hasErrors()) {
            String nat = timeSlotDao.getTimeSlot(reservation.getTimeSlotId()).getNaturalArea();
            model.addAttribute("naturalArea", nat);
            return "reservation/noReserva";
        }

        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        GeneratePDFController generatePDF = new GeneratePDFController();
        reservation.setCitizenEmail(citizen.getEmail());
        String zonas = reservation.getZoneid();
        String[] partes = zonas.split(",");
        int numRes = reservationDao.addReservationPocosValores(reservation);
        String naturalAreaName = naturalAreaDao.getNaturalAreaOfZone(partes[0]).getName();
        String timeSlotId = reservation.getTimeSlotId();
        TimeSlot timeSlot = timeSlotDao.getTimeSlot(timeSlotId);

        // Generar QR
        String text = "Reserva por " + reservation.getCitizenEmail() + " en " + naturalAreaName + ", de fecha " + reservation.getReservationDate()
                + ", de " + timeSlot.getBeginningTime() + " a " + timeSlot.getEndTime() + ", para " + reservation.getNumberOfPeople() + " personas.";
        generarQr(text, "" + numRes);

        String[] zonasBonito = new String[partes.length];
        for (int i = 0; i < partes.length; i++) {
            String zon = partes[i];
            Zone z = zoneDao.getZone(zon);
            zonasBonito[i] = z.getZoneNumber() + z.getLetter();
            reservation.setZoneid(zon);
            reservationDao.addReservationOfZone(numRes, reservation.getZoneid());
        }

        NaturalArea naturalArea = naturalAreaDao.getNaturalAreaOfZone(partes[0]);

        //Generamos el pdf
        try {
            Formatter formatter = new Formatter();
            String qr = "qr" + formatter.format("%07d", Integer.parseInt(""+numRes)) + ".png";
            formatter = new Formatter();
            File f = new File("pdfReserva" + formatter.format("%07d", Integer.parseInt(""+numRes)) + ".pdf");
            generatePDF.createPDF(f, citizen, reservation, naturalArea, qr, zonasBonito);
            byte[] bytes = Files.readAllBytes(f.toPath());
            Path path = Paths.get(uploadDirectory + "pdfs/" + f.getName());
            // Lo eliminamos de la carpeta errónea
            f.delete();
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Enviar mail con la reserva
        String destinatario = reservation.getCitizenEmail();
        String asunto = "Reserva completada";
        String cuerpo = "Reserva realizada correctamente el día " + reservation.getReservationDate() +
                " para " + reservation.getNumberOfPeople() + " personas. \n\nUn cordial saludo del equipo de SANA.";
        envioMailReserva(destinatario, asunto, cuerpo);

        return "redirect:/reservation/reservas"; //redirigim a la lista per a veure el reservation afegit
    }


    @RequestMapping("/cancelarReserva/{id}")
    public String cancelarReserva(@ModelAttribute("motivo") MotivoCancelancion motivo, @PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {
            });
            session.setAttribute("nextUrl", "/reservation/reservas");
            return "redirect:/inicio/login";
        }
        String mot = motivo.getMot();
        reservaDatosDao.cancelaReservaPorCiudadano(id, mot);

        // Actualizar QR con la cancelacion
        Reservation reservation = reservationDao.getReservation(Integer.parseInt(id));
        String text = "Reserva cancelada por " + reservation.getCitizenEmail() + " de fecha " + reservation.getReservationDate() + ".";
        generarQr(text, id);

        // Enviar mail con la cancelacion de la reserva
        String destinatario = reservation.getCitizenEmail();
        String asunto = "Reserva cancelada";
        String cuerpo = "Ha cancelado su reserva correctamente del día " + reservation.getReservationDate() +
                " para " + reservation.getNumberOfPeople() + " personas. \n\nUn cordial saludo del equipo de SANA.";
        envioMailReserva(destinatario, asunto, cuerpo);

        return "redirect:/reservation/reservas";
    }

    @RequestMapping("/update/{id}")
    public String updateReserva(@PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/reservation/reservas");
            return "redirect:/inicio/login";
        }

        int reservationId = Integer.parseInt(id);
        int maxCapacity = reservationDao.getMaximumCapacityOfReservation(reservationId);

        model.addAttribute("maxCapacity", maxCapacity);
        model.addAttribute("reservation", reservationDao.getReservation(reservationId));

        return "reservation/update";
    }

    @RequestMapping(value="/update", method= RequestMethod.POST)
    public String updateReservaPOST(@ModelAttribute("reservation") Reservation reservation) {
        int reservationId = reservation.getReservationNumber();
        int maxCapacity = reservationDao.getMaximumCapacityOfReservation(reservationId);
        String id = reservationId + "";

        //Validamos que el número de personas es correcto:
        if (reservation.getNumberOfPeople() > maxCapacity || reservation.getNumberOfPeople() <= 0)
            return "reservation/update/" + reservationId;
        else{
            reservaDatosDao.modificaReservaPersonas(id, reservation.getNumberOfPeople());

            // Actualizar QR con los nuevos datos de la reserva
            String timeSlotId = reservation.getTimeSlotId();
            TimeSlot timeSlot = timeSlotDao.getTimeSlot(timeSlotId);
            String text = "Reserva por " + reservation.getCitizenEmail() + ", de fecha " + reservation.getReservationDate()
                    + ", de " + timeSlot.getBeginningTime() + " a " + timeSlot.getEndTime() + ", para " + reservation.getNumberOfPeople() + " personas.";
            generarQr(text, id);

            // Enviar mail con la modificacion de la reserva
            String destinatario = reservation.getCitizenEmail();
            String asunto = "Reserva actualizada";
            String cuerpo = "Ha actualizado correctamente los datos de la reserva del día " + reservation.getReservationDate() +
                    " para " + reservation.getNumberOfPeople() + " personas. \n\nUn cordial saludo del equipo de SANA.";

            envioMailReserva(destinatario, asunto, cuerpo);
        }
        return "redirect:/reservation/reservas";
    }

    @RequestMapping("/updateManagers/{naturalArea}/{id}")
    public String editarReservaMuni(@PathVariable String naturalArea, @PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("municipalManager") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/reservation/reservas");
            return "redirect:/inicio/login";
        }

        int reservationId = Integer.parseInt(id);
        int maxCapacity = reservationDao.getMaximumCapacityOfReservation(reservationId);

        model.addAttribute("maxCapacity", maxCapacity);
        model.addAttribute("reservation", reservationDao.getReservation(reservationId));
        model.addAttribute("naturalArea", naturalArea);

        return "reservation/updateManagers";
    }

    @RequestMapping(value="/updateManagers/{naturalArea}", method= RequestMethod.POST)
    public String updateManagersPOST(@PathVariable String naturalArea, @ModelAttribute("reservation") Reservation reservation) {
        int reservationId = reservation.getReservationNumber();
        int maxCapacity = reservationDao.getMaximumCapacityOfReservation(reservationId);
        String id = reservationId + "";

        //Validamos que el número de personas es correcto:
        if (reservation.getNumberOfPeople() > maxCapacity || reservation.getNumberOfPeople() <= 0)
            return "reservation/updateManagers/" + naturalArea + "/" + reservationId;
        else{
            reservaDatosDao.modificaReservaPersonas(id, reservation.getNumberOfPeople());

            // Actualizar QR con los nuevos datos de la reserva
            String timeSlotId = reservation.getTimeSlotId();
            TimeSlot timeSlot = timeSlotDao.getTimeSlot(timeSlotId);
            String text = "Reserva por " + reservation.getCitizenEmail() + ", de fecha " + reservation.getReservationDate()
                    + ", de " + timeSlot.getBeginningTime() + " a " + timeSlot.getEndTime() + ", para " + reservation.getNumberOfPeople() + " personas.";
            generarQr(text, id);

            // Enviar mail con la modificacion de la reserva
            String destinatario = reservation.getCitizenEmail();
            String asunto = "Reserva actualizada";
            String cuerpo = "Debido a las restricciones de aforo en el área natural: " + naturalArea +
                    "se ha modificado el número de personas que podrán acudir a dicha reserva con fecha" + reservation.getReservationDate() +
                    " y se ha visto modificada a " + reservation.getNumberOfPeople() + " personas asistentes. \n\nUn cordial saludo del equipo de SANA.";
            envioMailReserva(destinatario, asunto, cuerpo);
        }
        return "redirect:/reservation/reservasArea/" + naturalArea;
    }

    @RequestMapping("/cancelarReservaMunicipal/{naturalArea}/{id}")
    public String cancelarReservaMunicipal(@ModelAttribute("motivo") MotivoCancelancion motivo, @PathVariable String naturalArea, @PathVariable String id, Model model, HttpSession session) {
        if (session.getAttribute("municipalManager") == null) {
            model.addAttribute("userLogin", new UserLogin() {
            });
            session.setAttribute("nextUrl", "/reservation/reservasArea/" + naturalArea);
            return "redirect:/inicio/login";
        }
        String mot = motivo.getMot();
        reservaDatosDao.cancelaReservaPorMunicipal(id, mot);

        // Actualizar QR con la cancelacion
        Reservation reservation = reservationDao.getReservation(Integer.parseInt(id));
        String text = "Reserva cancelada por gestor municipal a " + reservation.getCitizenEmail() + " de fecha " + reservation.getReservationDate() + ".";
        generarQr(text, id);

        // Enviar mail con la cancelacion de la reserva
        String destinatario = reservation.getCitizenEmail();
        String asunto = "Reserva cancelada";
        String cuerpo = "Su reserva del día " + reservation.getReservationDate() +
                " para " + reservation.getNumberOfPeople() + " personas ha sido cancelada por el gestor municipal." +
                "\n\nUn cordial saludo del equipo de SANA.";
        envioMailReserva(destinatario, asunto, cuerpo);

        return "redirect:/reservation/reservasArea/" + naturalArea;
    }

    @RequestMapping("/reservas")
    public String redirigirRegistradoReservas(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {
            });
            session.setAttribute("nextUrl", "/reservation/reservas");
            return "redirect:/inicio/login";
        }
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        model.addAttribute("citizenName", citizen.getName());
        List<ReservaDatos> listaReservas = reservaDatosDao.getReservasEmail(citizen.getEmail());
        List<ReservaDatosAgrupada> listaReservasAgrupadas = agruparPorReserva(listaReservas);
        model.addAttribute("motivo", new MotivoCancelancion());
        model.addAttribute("personas", new PersonasReserva());
        model.addAttribute("reservas", listaReservasAgrupadas);
        return "/reservation/reservas";
    }

    @RequestMapping("/reservasTodas")
    public String redirigirRegistradoReservasTodas(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {
            });
            session.setAttribute("nextUrl", "/reservation/reservasTodas");
            return "redirect:/inicio/login";
        }
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        model.addAttribute("citizenName", citizen.getName());
        List<ReservaDatos> listaReservas = reservaDatosDao.getReservasTodasEmail(citizen.getEmail());
        List<ReservaDatosAgrupada> listaReservasAgrupadas = agruparPorReserva(listaReservas);
        model.addAttribute("reservas", listaReservasAgrupadas);
        return "/reservation/reservasTodas";
    }

    @RequestMapping(value = "/reservasArea/{naturalArea}")
    public String reservasArea(Model model, @PathVariable String naturalArea, HttpSession session) {
        if (session.getAttribute("municipalManager") == null) {
            model.addAttribute("userLogin", new UserLogin() {
            });
            session.setAttribute("nextUrl", "/reservation/reservasArea/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("reservas", agruparPorReservaMuni(reservaDatosDao.getReservasNaturalArea(naturalArea)));
        model.addAttribute("motivo", new MotivoCancelancion());
        model.addAttribute("personas", new PersonasReserva());
        model.addAttribute("naturalArea", naturalArea);
        return "reservation/reservasArea";
    }

    @RequestMapping(value = "/reservasTodasArea/{naturalArea}")
    public String todasReservasnaturalArea(Model model, @PathVariable String naturalArea, HttpSession session) {
        if (session.getAttribute("municipalManager") == null) {
            model.addAttribute("userLogin", new UserLogin() {
            });
            session.setAttribute("nextUrl", "/reservation/reservasTodasArea/" + naturalArea);
            return "redirect:/inicio/login";
        }

        List<ReservaDatosMunicipal> listaReservas = reservaDatosDao.getReservasTodasNaturalArea(naturalArea);
        List<ReservaDatosAgrupadaMunicipal> listaReservasAgrupada = agruparPorReservaMuni(listaReservas);
        model.addAttribute("reservas", listaReservasAgrupada);
        model.addAttribute("naturalArea", naturalArea);
        return "reservation/reservasTodasArea";
    }

    private void generarQr (String text, String id) {
        Formatter fmt = new Formatter();
        QRCode qr = new QRCode();
        File f = new File("qr" + fmt.format("%07d", Integer.parseInt(id)) + ".png");
        try {
            qr.generateQR(f, text, 300, 300);
            byte[] bytes = Files.readAllBytes(f.toPath());
            Path path = Paths.get(uploadDirectory + "qrCodes/" + f.getName());
            // Lo eliminamos de la carpeta errónea
            f.delete();
            Files.write(path, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void envioMailReserva (String destinatario, String asunto, String cuerpo) {
        Email email = HomeController.enviarMail(destinatario, asunto, cuerpo);
        emailDao.addEmail(email);
    }

    private List<ReservaDatosAgrupada> agruparPorReserva(List<ReservaDatos> listaReservas) {
        List<ReservaDatosAgrupada> listaReservasAgrupadas = new ArrayList<>();
        for (ReservaDatos res : listaReservas){
            boolean esta = false;
            for ( ReservaDatosAgrupada agrupada : listaReservasAgrupadas) {
                if (agrupada.getReservationNumber() == res.getReservationNumber()) {
                    esta = true;
                    List<String> zonas = agrupada.getZonas();
                    zonas.add(res.getZoneNumber() + " " + res.getLetter());
                    agrupada.setZonas(zonas);
                    break;
                }
            }
            if (!esta){
                ReservaDatosAgrupada nuevaReserva = new ReservaDatosAgrupada();
                nuevaReserva.setReservationNumber(res.getReservationNumber());
                nuevaReserva.setReservationDate(res.getReservationDate());
                nuevaReserva.setNumberOfPeople(res.getNumberOfPeople());
                nuevaReserva.setState(res.getState());
                nuevaReserva.setQRcode(res.getQRcode());
                List<String> zones = new ArrayList<>();
                zones.add(res.getZoneNumber() + " " + res.getLetter());
                nuevaReserva.setZonas(zones);
                nuevaReserva.setNaturalArea(res.getNaturalArea());
                nuevaReserva.setBeginningTime(res.getBeginningTime());
                nuevaReserva.setEndTime(res.getEndTime());
                listaReservasAgrupadas.add(nuevaReserva);
            }
        }
        return listaReservasAgrupadas;
    }

    private List<ReservaDatosAgrupadaMunicipal> agruparPorReservaMuni(List<ReservaDatosMunicipal> listaReservas) {
        List<ReservaDatosAgrupadaMunicipal> listaReservasAgrupadas = new ArrayList<>();
        for (ReservaDatosMunicipal res : listaReservas){
            boolean esta = false;
            for ( ReservaDatosAgrupadaMunicipal agrupada : listaReservasAgrupadas) {
                if (agrupada.getReservationNumber() == res.getReservationNumber()) {
                    esta = true;
                    List<String> zonas = agrupada.getZonas();
                    zonas.add(res.getZoneNumber() + " " + res.getLetter());
                    agrupada.setZonas(zonas);
                    break;
                }
            }
            if (!esta){
                ReservaDatosAgrupadaMunicipal nuevaReserva = new ReservaDatosAgrupadaMunicipal();
                nuevaReserva.setReservationNumber(res.getReservationNumber());
                nuevaReserva.setReservationDate(res.getReservationDate());
                nuevaReserva.setNumberOfPeople(res.getNumberOfPeople());
                nuevaReserva.setState(res.getState());
                nuevaReserva.setQRcode(res.getQRcode());
                List<String> zones = new ArrayList<>();
                zones.add(res.getZoneNumber() + " " + res.getLetter());
                nuevaReserva.setZonas(zones);
                nuevaReserva.setNaturalArea(res.getNaturalArea());
                nuevaReserva.setBeginningTime(res.getBeginningTime());
                nuevaReserva.setEndTime(res.getEndTime());
                nuevaReserva.setName(res.getName());
                nuevaReserva.setSurname(res.getSurname());
                listaReservasAgrupadas.add(nuevaReserva);
            }
        }
        return listaReservasAgrupadas;
    }
}