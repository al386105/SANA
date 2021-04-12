package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReceiverDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el receiver a la base de dades */
    public void addReceiver(Receiver receiver) {
        jdbcTemplate.update(
                "INSERT INTO Receiver VALUES(?, ?, ?, ?, ?, ?)",
                receiver.getEmail(), receiver.getName(), receiver.getSurname(), receiver.getDateOfBirth(),
                LocalDate.now(), null);
    }

    /* Esborra el receiver de la base de dades */
    public void deleteReceiver(String email) {
        jdbcTemplate.update("DELETE FROM Receiver WHERE email =?", email);
    }

    /* Actualitza els atributs del receiver (excepte el email, que és la clau primària) */
    public void updateReceiver(Receiver receiver) {
        jdbcTemplate.update("UPDATE Receiver SET name = ?, surname = ?, dateOfBirth = ?, registrationDate = ?, " +
                        "leavingDate = ? WHERE email =?", receiver.getName(), receiver.getSurname(),
                receiver.getDateOfBirth(), receiver.getRegistrationDate(), receiver.getLeavingDate(),
                receiver.getEmail());
    }

    /* Obté el receiver amb el email donat. Torna null si no existeix. */
    public Receiver getReceiver(String email) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM Receiver WHERE email =?",
                    new ReceiverRowMapper(), email);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté tots els receivers. Torna una llista buida si no n'hi ha cap. */
    public List<Receiver> getReceivers() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Receiver",
                    new ReceiverRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Receiver>();
        }
    }
}

