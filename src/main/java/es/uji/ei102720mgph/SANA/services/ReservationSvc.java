package es.uji.ei102720mgph.SANA.services;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationSvc implements ReservationService {
    @Autowired
    ReservationDao reservationDao;

    @Override
    public void addReservation(Reservation reservation) {
        String nombreImagenQR = reservation.getQRcode();
        reservation.setQRcode("assets/img/qrCodes/" + nombreImagenQR);
        reservationDao.addReservation(reservation); //usem el dao per a inserir el reservation
    }
}
