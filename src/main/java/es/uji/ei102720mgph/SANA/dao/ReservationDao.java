package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.ReservationState;
import es.uji.ei102720mgph.SANA.model.NuevaReserva;
import es.uji.ei102720mgph.SANA.model.Reservation;
import es.uji.ei102720mgph.SANA.model.ReservationOfZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

@Repository
public class ReservationDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el reservation a la base de dades */
    public void addReservation(Reservation reservation) {
        boolean excepcion;
        Formatter fmt;
        do {
            try {
                fmt = new Formatter();
                jdbcTemplate.update(
                        "INSERT INTO Reservation VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Reservation.getContador(), reservation.getReservationDate(), LocalDate.now(),
                        LocalTime.now(), reservation.getNumberOfPeople(), reservation.getState().name(),
                        reservation.getQRcode(), null, null, reservation.getCitizenEmail(), reservation.getTimeSlotId());

                excepcion = false;
            } catch (DuplicateKeyException e) {
                excepcion = true;
            } finally {
                Reservation.incrementaContador();
            }
        } while (excepcion);
    }

    public int addReservationPocosValores(NuevaReserva reservation) {
        boolean excepcion;
        Formatter fmt;
        do {
            try {
                fmt = new Formatter();
                jdbcTemplate.update(
                        "INSERT INTO Reservation VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Reservation.getContador(), reservation.getReservationDate(), LocalDate.now(),
                        LocalTime.now(), reservation.getNumberOfPeople(), ReservationState.created.name(),
                        "qrCodes/qr"+ fmt.format("%07d", Reservation.getContador()), null, null, reservation.getCitizenEmail(), reservation.getTimeSlotId());

                excepcion = false;
            } catch (DuplicateKeyException e) {
                excepcion = true;
            } finally {
                Reservation.incrementaContador();
            }
        } while (excepcion);
        return Reservation.getContador()-1;
    }

    public void addReservationOfZone(int numRes, String zoneNumber) {
        boolean excepcion;
        Formatter fmt;
        do {
            try {
                fmt = new Formatter();
                jdbcTemplate.update(
                        "INSERT INTO ReservationOfZone VALUES(?, ?, ?)",
                        "" + fmt.format("%06d", ReservationOfZone.getContador()), numRes, zoneNumber);

                excepcion = false;
            } catch (DuplicateKeyException e) {
                excepcion = true;
            } finally {
                ReservationOfZone.incrementaContador();
            }
        } while (excepcion);
    }

    /* Actualitza els atributs del reservation */
    public void updateReservation(Reservation reservation) {
        jdbcTemplate.update("UPDATE Reservation SET reservationDate = ?, creationDate = ?, creationTime = ?, " +
                        "numberOfPeople = ?, state = ?, QRcode = ?, cancellationDate = ?, cancellationReason = ?, " +
                        "citizenEmail = ?, timeSlotId = ? WHERE reservationNumber =?",
                reservation.getReservationDate(), reservation.getCreationDate(), reservation.getCreationTime(),
                reservation.getNumberOfPeople(), reservation.getState().name(), reservation.getQRcode(),
                reservation.getCancellationDate(), reservation.getCancellationReason(), reservation.getCitizenEmail(),
                reservation.getTimeSlotId(), reservation.getReservationNumber());
    }

    /* Obté el reservation amb el reservationNumber donat. Torna null si no existeix. */
    public Reservation getReservation(int reservationNumber) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM Reservation WHERE reservationNumber =?",
                    new ReservationRowMapper(), reservationNumber);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Integer getMaximumCapacity(int reservationNumber) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT z.maximumcapacity FROM reservation AS r " +
                            "JOIN reservationofzone AS res ON r.reservationnumber = res.reservationnumber " +
                            "JOIN zone AS z ON res.zoneid = z.id " +
                            "WHERE r.reservationnumber = ?",
                    Integer.class, reservationNumber);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Devuelve todas las reservas realizadas en el area natural
     * */
    public List<Reservation> getReservationsOfNaturalArea(String naturalArea) {
        try {
            return jdbcTemplate.query("SELECT * FROM Reservation " +
                    "JOIN ReservationOfZone ON Reservation.reservationNumber = ReservationOfZone.reservationNumber " +
                    "JOIN Zone ON ReservationOfZone.zoneId = Zone.id " +
                    "WHERE Zone.naturalArea = ?",
                    new ReservationRowMapper(),
                    naturalArea);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Reservation>();
        }
    }

    /**
     * Devuelve todas las reservas del año recibido (date) del area natural
     * */
    public List<Reservation> getReservationsOfNaturalAreaOfYear(String naturalArea, LocalDate date) {
        try {
            return jdbcTemplate.query("SELECT * FROM Reservation " +
                            "JOIN ReservationOfZone ON Reservation.reservationNumber = ReservationOfZone.reservationNumber " +
                            "JOIN Zone ON ReservationOfZone.zoneId = Zone.id " +
                            "WHERE Zone.naturalArea = ? " +
                            "AND EXTRACT(year FROM Reservation.reservationDate) = ? ",
                    new ReservationRowMapper(),
                    naturalArea, date.getYear());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Reservation>();
        }
    }

    /**
     * Devuelve todas las reservas del mes recibido (date) del area natural
     * */
    public List<Reservation> getReservationsOfNaturalAreaOfMonth(String naturalArea, LocalDate date) {
        try {
            return jdbcTemplate.query("SELECT * FROM Reservation " +
                            "JOIN ReservationOfZone ON Reservation.reservationNumber = ReservationOfZone.reservationNumber " +
                            "JOIN Zone ON ReservationOfZone.zoneId = Zone.id " +
                            "WHERE Zone.naturalArea = ? " +
                            "AND EXTRACT(year FROM Reservation.reservationDate) = ? " +
                            "AND EXTRACT(month FROM Reservation.reservationDate) = ? ",
                    new ReservationRowMapper(),
                    naturalArea, date.getYear(), date.getMonthValue());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Reservation>();
        }
    }

    /**
     * Devuelve todas las reservas de un determinado dia del area natural
     * */
    public List<Reservation> getReservationsOfNaturalAreaOfDay(String naturalArea, LocalDate date) {
        try {
            return jdbcTemplate.query("SELECT * FROM Reservation " +
                            "JOIN ReservationOfZone ON Reservation.reservationNumber = ReservationOfZone.reservationNumber " +
                            "JOIN Zone ON ReservationOfZone.zoneId = Zone.id " +
                            "WHERE Zone.naturalArea = ? AND Reservation.reservationDate = ?",
                    new ReservationRowMapper(),
                    naturalArea, date);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Reservation>();
        }
    }

    /**
     * Devuelve todas las reservas de una hora y dia determinado del area natural
     * */
    public List<Reservation> getReservationsOfNaturalAreaOfHour(String naturalArea, LocalDate date, LocalTime time) {
        try {
            return jdbcTemplate.query("SELECT * FROM Reservation " +
                            "JOIN ReservationOfZone ON Reservation.reservationNumber = ReservationOfZone.reservationNumber " +
                            "JOIN Zone ON ReservationOfZone.zoneId = Zone.id " +
                            "JOIN TimeSlot ON Reservation.timeSlotId = TimeSlot.id " +
                            "WHERE Zone.naturalArea = ? AND Reservation.reservationDate = ? " +
                            "AND TimeSlot.beginningTime <= ? AND endTime >= ? ",
                    new ReservationRowMapper(),
                    naturalArea, date, time, time);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Reservation>();
        }
    }

    /* Obté tots els reservations. Torna una llista buida si no n'hi ha cap. */
    public List<Reservation> getReservations() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Reservation",
                    new ReservationRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Reservation>();
        }
    }
}

