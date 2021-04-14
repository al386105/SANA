package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AddressDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el address a la base de dades */
    public void addAddress(Address address) {
        jdbcTemplate.update(
                "INSERT INTO Address VALUES(?, ?, ?, ?, ?, ?, ?)",
                address.getId(), address.getStreet(), address.getNumber(), address.getFloorDoor(),
                address.getPostalCode(), address.getCity(), address.getCountry());
    }

    /* Esborra el address de la base de dades */
    public void deleteAddress(String idAddress) {
        jdbcTemplate.update("DELETE FROM Address WHERE id =?", idAddress);
    }

    /* Actualitza els atributs del address (excepte el id, que és la clau primària) */
    public void updateAddress(Address address) {
        jdbcTemplate.update("UPDATE Address SET street = ?, number = ?, floorDoor = ?, postalCode = ?, " +
                        "city = ?, country = ? WHERE id =?",
                address.getStreet(), address.getNumber(), address.getFloorDoor(),
                address.getPostalCode(), address.getCity(), address.getCountry(), address.getId());
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

    /* Obté tots els addresss. Torna una llista buida si no n'hi ha cap. */
    public List<Address> getAddresses() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Address",
                    new AddressRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Address>();
        }
    }
}
