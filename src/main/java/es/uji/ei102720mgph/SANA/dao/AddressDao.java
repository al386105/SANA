package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.Formatter;

@Repository
public class AddressDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el address a la base de dades */
    public void addAddress(Address address) {
        boolean excepcion;
        Formatter fmt;
        do {
            try {
                fmt = new Formatter();
                jdbcTemplate.update("INSERT INTO Address VALUES(?, ?, ?, ?, ?, ?, ?)",
                        "ad" + fmt.format("%07d", Address.getContador()), address.getStreet(), address.getNumber(),
                        address.getFloorDoor(), address.getPostalCode(), address.getCity(), address.getCountry());
                excepcion = false;
            } catch (DuplicateKeyException e) {
                excepcion = true;
            } finally {
                Address.incrementaContador();
            }
        } while (excepcion);
    }

    /* Obté el address amb el id donat. Torna null si no existeix. */
    public Address getAddress(String idAddress) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM Address WHERE id =?",
                    new AddressRowMapper(), idAddress);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }
}
