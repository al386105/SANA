package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.ReservationState;
import es.uji.ei102720mgph.SANA.model.ReservaDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservaDatosDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<ReservaDatos> getReservasEmail(String email) {
        try {
            return jdbcTemplate.query("SELECT res.reservationnumber, res.reservationdate, res.numberofpeople, res.state, res.qrcode, zone.zonenumber, zone.letter, zone.naturalarea " +
                            "FROM reservation AS res " +
                            "JOIN reservationofzone AS ro ON res.reservationnumber = ro.reservationnumber " +
                            "JOIN zone AS zone ON ro.zoneid = zone.id " +
                            "WHERE res.citizenemail = ? AND (res.state = 'created' OR res.state = 'inUse')",
                    new ReservaDatosRowMapper(),
                    email);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<ReservaDatos>();
        }
    }

    public ReservaDatos getReservation(int reservationNumber) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT res.reservationnumber, res.reservationdate, res.numberofpeople, res.state, res.qrcode, zone.zonenumber, zone.letter, zone.naturalarea " +
                            "FROM reservation AS res " +
                            "JOIN reservationofzone AS ro ON res.reservationnumber = ro.reservationnumber " +
                            "JOIN zone AS zone ON ro.zoneid = zone.id " +
                            "WHERE res.reservationNumber = ?",
                    new ReservaDatosRowMapper(), reservationNumber);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<ReservaDatos> getReservasTodasEmail(String email) {
        try {
            return jdbcTemplate.query("SELECT res.reservationnumber, res.reservationdate, res.numberofpeople, res.state, res.qrcode, zone.zonenumber, zone.letter, zone.naturalarea " +
                            "FROM reservation AS res " +
                            "JOIN reservationofzone AS ro ON res.reservationnumber = ro.reservationnumber " +
                            "JOIN zone AS zone ON ro.zoneid = zone.id " +
                            "WHERE res.citizenemail = ?",
                    new ReservaDatosRowMapper(),
                    email);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<ReservaDatos>();
        }
    }

    public void cancelaReservaPorCiudadano(String id){
        jdbcTemplate.update("UPDATE Reservation SET state = ? WHERE reservationNumber =?", ReservationState.cancelledCitizen.name(), Integer.parseInt(id));
    }
}
