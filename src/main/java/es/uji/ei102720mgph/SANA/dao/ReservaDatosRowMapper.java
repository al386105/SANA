package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.ReservationState;
import es.uji.ei102720mgph.SANA.model.ReservaDatos;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaDatosRowMapper implements RowMapper<ReservaDatos> {
    public ReservaDatos mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReservaDatos reserva = new ReservaDatos();
        reserva.setReservationNumber(rs.getInt("reservationNumber"));
        reserva.setReservationDate(rs.getObject("reservationDate", LocalDate.class));
        reserva.setNumberOfPeople(rs.getInt("numberOfPeople"));
        ReservationState s = ReservationState.valueOf(rs.getString("state"));
        reserva.setState(s);
        reserva.setQRcode(rs.getString("QRcode"));
        reserva.setZoneNumber(rs.getInt("zoneNumber"));
        reserva.setLetter(rs.getString("letter").charAt(0));
        reserva.setNaturalArea(rs.getString("naturalArea"));
        reserva.setBeginningTime(rs.getObject("beginningTime", LocalTime.class));
        reserva.setEndTime(rs.getObject("endTime", LocalTime.class));
        return reserva;
    }
}
