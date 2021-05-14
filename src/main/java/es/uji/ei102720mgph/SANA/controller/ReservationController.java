package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ReservationDao;
import es.uji.ei102720mgph.SANA.dao.TimeSlotDao;
import es.uji.ei102720mgph.SANA.enums.ReservationState;
import es.uji.ei102720mgph.SANA.model.NuevaReserva;
import es.uji.ei102720mgph.SANA.model.Reservation;
import es.uji.ei102720mgph.SANA.services.NaturalAreaService;
import es.uji.ei102720mgph.SANA.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private ReservationDao reservationDao;
    private ReservationService reservationService;
    private TimeSlotDao timeSlotDao;

    @Autowired
    public void setReservationDao(ReservationDao reservationDao) {
        this.reservationDao=reservationDao;
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
    @RequestMapping("/list")
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationDao.getReservations());
        return "reservation/list";
    }

    // Operació crear
    @RequestMapping(value="/add/{naturalArea}")
    public String addReservation(Model model, @PathVariable String naturalArea, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) return "redirect:/inicio/login";
        model.addAttribute("reservation", new NuevaReserva());
        model.addAttribute("naturalArea", naturalArea);
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlotNaturalArea(naturalArea));
        return "reservation/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("reservation") NuevaReserva reservation,
                                   BindingResult bindingResult) {
        //ReservationValidator reservationValidator = new ReservationValidator();
        //reservationValidator.validate(reservation, bindingResult);

        if (bindingResult.hasErrors())
            return "reservation/add"; //tornem al formulari per a que el corregisca


        //reservationService.addReservation(reservation);
        System.out.println(reservation);

        return "redirect:/inicio/registrado/reservas"; //redirigim a la lista per a veure el reservation afegit, post/redirect/get
    }

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
        return "redirect:/reservation/listManagers/";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{reservationNumber}")
    public String processDelete(@PathVariable int reservationNumber) {
        reservationDao.deleteReservation(reservationNumber);
        return "redirect:../list";
    }
}