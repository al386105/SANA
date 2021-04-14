package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MunicipalManagerDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addMunicipalManager(MunicipalManager manager) {
        jdbcTemplate.update(
                "INSERT INTO Receiver VALUES(?, ?, ?, ?, ?, ?)",
                manager.getEmail(), manager.getName(), manager.getSurname(), manager.getDateOfBirth(),
                LocalDate.now(), null);
        jdbcTemplate.update(
                "INSERT INTO MunicipalManager VALUES(?, ?, ?, ?)",
                manager.getEmail(), manager.getUsername(), manager.getPassword(), manager.getMunicipality());

    }

    public void deleteMunicipalManager(String email) {
        jdbcTemplate.update("DELETE FROM MunicipalManager WHERE email =?", email);
        jdbcTemplate.update("DELETE FROM Receiver WHERE email =?", email);
    }

    public void updateMunicipalManager(MunicipalManager manager) {
        jdbcTemplate.update("UPDATE MunicipalManager SET username = ?, password = ?, municipality = ? WHERE email =?",
                manager.getUsername(), manager.getPassword(), manager.getMunicipality(), manager.getEmail());
        jdbcTemplate.update("UPDATE Receiver SET name = ?, surname = ?, dateOfBirth = ?, " +
                        "registrationDate = ?, leavingDate = ? " +
                        "WHERE email =?", manager.getName(), manager.getSurname(),
                manager.getDateOfBirth(), manager.getRegistrationDate(), manager.getLeavingDate(),
                manager.getEmail());
    }

    public MunicipalManager getMunicipalManager(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM MunicipalManager " +
                            "JOIN Receiver ON MunicipalManager.email = Receiver.email " +
                            "WHERE Receiver.email = ? ",
                    new MunicipalManagerRowMapper(), email);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<MunicipalManager> getMunicipalManagers() {
        try {
            return jdbcTemplate.query("SELECT * FROM MunicipalManager " +
                            "JOIN Receiver ON MunicipalManager.email = Receiver.email",
                    new MunicipalManagerRowMapper());

        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<MunicipalManager>();
        }
    }
}
