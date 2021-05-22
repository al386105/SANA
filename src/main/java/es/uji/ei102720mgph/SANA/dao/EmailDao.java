package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Address;
import es.uji.ei102720mgph.SANA.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Formatter;

@Repository
public class EmailDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el email a la base de dades */
    public void addEmail(Email email) {
        boolean excepcion;
        Formatter fmt;
        do {
            try {
                fmt = new Formatter();
                jdbcTemplate.update("INSERT INTO Email VALUES(?, ?, ?, ?, ?, ?)",
                        "" + fmt.format("%06d", Address.getContador()), email.getSubject(), email.getTextBody(),
                        "sana.espais.naturals@gmail.com", LocalDate.now(), email.getSanaUser());
                excepcion = false;
            } catch (DuplicateKeyException e) {
                excepcion = true;
            } finally {
                Address.incrementaContador();
            }
        } while (excepcion);
    }
}
