package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmailDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el email a la base de dades */
    public void addEmail(Email email) {
        jdbcTemplate.update(
                "INSERT INTO Email VALUES(?, ?, ?, ?, ?, ?)",
                email.getId(), email.getSubject(), email.getTextBody(), email.getSender(),
                LocalDate.now(), email.getSanaUser());
    }

    /* Esborra el email de la base de dades */
    public void deleteEmail(String id) {
        jdbcTemplate.update("DELETE FROM Email WHERE id =?", id);
    }

    /* Actualitza els atributs del email (excepte el id, que és la clau primària) */
    public void updateEmail(Email email) {
        jdbcTemplate.update("UPDATE Email SET subject = ?, textBody = ?, sender = ?, date = ?, sanaUser = ?" +
                        " WHERE id =?",
                email.getSubject(), email.getTextBody(), email.getSender(), email.getDate(), email.getSanaUser(),
                email.getId());
    }

    /* Obté el email amb el id donat. Torna null si no existeix. */
    public Email getEmail(String id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM Email WHERE id =?",
                    new EmailRowMapper(), id);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté tots els emails. Torna una llista buida si no n'hi ha cap. */
    public List<Email> getEmails() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Email",
                    new EmailRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Email>();
        }
    }
}
