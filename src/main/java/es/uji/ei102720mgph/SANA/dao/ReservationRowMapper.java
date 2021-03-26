package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.ReservationState;
import es.uji.ei102720mgph.SANA.model.Reservation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public final class ReservationRowMapper implements RowMapper<Reservation> {

    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationNumber(rs.getInt("reservationNumber"));
        reservation.setReservationDate(rs.getObject("reservationDate", LocalDate.class));
        reservation.setCreationDate(rs.getObject("creationDate", LocalDate.class));
        reservation.setCreationTime(rs.getObject("creationTime", LocalTime.class));
        reservation.setNumberOfPeople(rs.getInt("numberOfPeople"));
        reservation.setState(ReservationState.values()[rs.getInt("state")]);
        reservation.setQRcode(rs.getString("QRcode"));
        Date d = rs.getDate("cancellationDate");
        reservation.setCancellationDate(d != null ? d.toLocalDate() : null);
        reservation.setCancellationReason(rs.getString("cancellationReason"));
        reservation.setCitizenEmail(rs.getString("citizenEmail"));
        reservation.setTimeSlotId(rs.getString("timeSlotId"));
        return reservation;
    }
}
