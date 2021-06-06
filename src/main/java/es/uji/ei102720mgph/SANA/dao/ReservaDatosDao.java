package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.ReservationState;
import es.uji.ei102720mgph.SANA.model.ReservaDatos;
import es.uji.ei102720mgph.SANA.model.ReservaDatosMunicipal;
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
            return jdbcTemplate.query("SELECT res.reservationnumber, res.reservationdate, res.numberofpeople, res.state, res.qrcode, zone.zonenumber, zone.letter, zone.naturalarea, slot.beginningtime, slot.endtime " +
                            "FROM reservation AS res " +
                            "JOIN reservationofzone AS ro ON res.reservationnumber = ro.reservationnumber " +
                            "JOIN zone AS zone ON ro.zoneid = zone.id " +
                            "JOIN timeslot AS slot ON res.timeslotid = slot.id " +
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
            return jdbcTemplate.query("SELECT res.reservationnumber, res.reservationdate, res.numberofpeople, res.state, res.qrcode, zone.zonenumber, zone.letter, zone.naturalarea, slot.beginningtime, slot.endtime " +
                            "FROM reservation AS res " +
                            "JOIN reservationofzone AS ro ON res.reservationnumber = ro.reservationnumber " +
                            "JOIN zone AS zone ON ro.zoneid = zone.id " +
                            "JOIN timeslot AS slot ON res.timeslotid = slot.id " +
                            "WHERE res.citizenemail = ?",
                    new ReservaDatosRowMapper(),
                    email);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<ReservaDatos>();
        }
    }

    public void cancelaReservaPorCiudadano(String id, String mot){
        jdbcTemplate.update("UPDATE Reservation SET state = ?, cancellationreason = ? WHERE reservationNumber =?", ReservationState.cancelledCitizen.name(), mot, Integer.parseInt(id));
    }

    public void modificaReservaPersonas(String id, int personas){
        jdbcTemplate.update("UPDATE Reservation SET numberofpeople = ? WHERE reservationNumber =?", personas, Integer.parseInt(id));
    }

    public void cancelaReservaPorMunicipal(String id, String mot){
        jdbcTemplate.update("UPDATE Reservation SET state = ?, cancellationreason = ? WHERE reservationNumber =?", ReservationState.cancelledMunicipalManager.name(), mot, Integer.parseInt(id));
    }

    public List<ReservaDatosMunicipal> getReservasNaturalArea(String naturalArea) {
        try {
            return jdbcTemplate.query("SELECT res.reservationnumber, res.reservationdate, res.numberofpeople, res.state, res.qrcode, zone.zonenumber, zone.letter, zone.naturalarea, slot.beginningtime, slot.endtime, cit.name, cit.surname " +
                            "FROM reservation AS res " +
                            "JOIN reservationofzone AS ro ON res.reservationnumber = ro.reservationnumber " +
                            "JOIN zone AS zone ON ro.zoneid = zone.id " +
                            "JOIN timeslot AS slot ON res.timeslotid = slot.id " +
                            "JOIN sanauser AS cit ON res.citizenemail = cit.email " +
                            "WHERE zone.naturalarea = ? AND (res.state = 'created' OR res.state = 'inUse')",
                    new ReservaDatosMunicipalRowMapper(), naturalArea);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<ReservaDatosMunicipal>();
        }
    }

    public List<ReservaDatosMunicipal> getReservasTodasNaturalArea(String naturalArea) {
        try {
            return jdbcTemplate.query("SELECT res.reservationnumber, res.reservationdate, res.numberofpeople, res.state, res.qrcode, zone.zonenumber, zone.letter, zone.naturalarea, slot.beginningtime, slot.endtime, cit.name, cit.surname " +
                            "FROM reservation AS res " +
                            "JOIN reservationofzone AS ro ON res.reservationnumber = ro.reservationnumber " +
                            "JOIN zone AS zone ON ro.zoneid = zone.id " +
                            "JOIN timeslot AS slot ON res.timeslotid = slot.id " +
                            "JOIN sanauser AS cit ON res.citizenemail = cit.email " +
                            "WHERE zone.naturalarea = ?",
                    new ReservaDatosMunicipalRowMapper(), naturalArea);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<ReservaDatosMunicipal>();
        }
    }
}
