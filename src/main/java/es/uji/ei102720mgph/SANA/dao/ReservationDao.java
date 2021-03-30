package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
        jdbcTemplate.update(
                "INSERT INTO Reservation VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                reservation.getReservationNumber(), reservation.getReservationDate(), LocalDate.now(),
                LocalTime.now(), reservation.getNumberOfPeople(), reservation.getState().name(), reservation.getQRcode(),
                null, null, reservation.getCitizenEmail(), reservation.getTimeSlotId());
    }

    /* Esborra el reservation de la base de dades */
    public void deleteReservation(int reservationNumber) {
        jdbcTemplate.update("DELETE FROM Reservation WHERE reservationNumber =?", reservationNumber);
    }

    /* Actualitza els atributs del reservation */
    public void updateReservation(Reservation reservation) {
        jdbcTemplate.update("UPDATE Reservation SET reservationDate = ?, creationDate = ?, creationTime = ?, " +
                        "numberOfPeople = ?, state = ?, QRcode = ?, cancellationDate = ?, cancellationReason = ?," +
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

