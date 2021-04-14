package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ReservationDao;
import es.uji.ei102720mgph.SANA.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private ReservationDao reservationDao;

    @Autowired
    public void setReservationDao(ReservationDao reservationDao) {
        this.reservationDao=reservationDao;
    }

    // Operació llistar
    @RequestMapping("/list")
    public String listReservations(Model model) {
        model.addAttribute("reservations", reservationDao.getReservations());
        return "reservation/list";
    }

    // Operació crear
    @RequestMapping(value="/add")
    public String addReservation(Model model) {
        model.addAttribute("reservation", new Reservation());
        return "reservation/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("reservation") Reservation reservation,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "reservation/add"; //tornem al formulari per a que el corregisca
        String nombreImagenQR = reservation.getQRcode();
        reservation.setQRcode("assets/img/qrCodes/" + nombreImagenQR);
        reservationDao.addReservation(reservation); //usem el dao per a inserir el reservation
        return "redirect:list"; //redirigim a la lista per a veure el reservation afegit, post/redirect/get
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

    // Operació esborrar
    @RequestMapping(value="/delete/{reservationNumber}")
    public String processDelete(@PathVariable int reservationNumber) {
        reservationDao.deleteReservation(reservationNumber);
        return "redirect:../list";
    }
}