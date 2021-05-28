package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.TypeOfUser;
import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import es.uji.ei102720mgph.SANA.model.Municipality;
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
                "INSERT INTO SanaUser VALUES(?, ?, ?, ?, ?, ?, ?)",
                manager.getEmail(), manager.getName(), manager.getSurname(), manager.getDateOfBirth(),
                LocalDate.now(), null, TypeOfUser.municipalManager.name());
        jdbcTemplate.update(
                "INSERT INTO MunicipalManager VALUES(?, ?, ?, ?)",
                manager.getEmail(), manager.getUsername(), manager.getPassword(), manager.getMunicipality());
    }

    public void deleteMunicipalManager(String email) {
        jdbcTemplate.update("DELETE FROM MunicipalManager WHERE email =?", email);
        jdbcTemplate.update("DELETE FROM SanaUser WHERE email =?", email);
    }

    public void updateMunicipalManager(MunicipalManager manager) {
        jdbcTemplate.update("UPDATE MunicipalManager SET username = ?, password = ?, municipality = ? WHERE email =?",
                manager.getUsername(), manager.getPassword(), manager.getMunicipality(), manager.getEmail());
        jdbcTemplate.update("UPDATE SanaUser SET name = ?, surname = ?, dateOfBirth = ?, " +
                        "registrationDate = ?, leavingDate = ?, typeOfUser = ? " +
                        "WHERE email =?", manager.getName(), manager.getSurname(),
                manager.getDateOfBirth(), manager.getRegistrationDate(), manager.getLeavingDate(),
                TypeOfUser.municipalManager.name(), manager.getEmail());
    }

    public MunicipalManager getMunicipalManager(String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM MunicipalManager " +
                            "JOIN SanaUser ON MunicipalManager.email = SanaUser.email " +
                            "WHERE SanaUser.email = ? ",
                    new MunicipalManagerRowMapper(), email);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public MunicipalManager getMunicipalManagerUsername(String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM MunicipalManager " +
                            "JOIN SanaUser ON MunicipalManager.email = SanaUser.email " +
                            "WHERE MunicipalManager.username = ? ",
                    new MunicipalManagerRowMapper(), username);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<MunicipalManager> getManagersOfMunicipality(String municipality) {
        try {
            return jdbcTemplate.query("SELECT * FROM MunicipalManager " +
                            "JOIN SanaUser ON MunicipalManager.email = SanaUser.email WHERE municipality = ?",
                    new MunicipalManagerRowMapper(),
                    municipality);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<MunicipalManager>();
        }
    }

    public List<MunicipalManager> getMunicipalManagers() {
        try {
            return jdbcTemplate.query("SELECT * FROM MunicipalManager " +
                            "JOIN SanaUser ON MunicipalManager.email = SanaUser.email",
                    new MunicipalManagerRowMapper());

        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<MunicipalManager>();
        }
    }

    public  List<MunicipalManager> getMunicipalManagersSearch(String patron) {
        try {
            return jdbcTemplate.query("SELECT * FROM MunicipalManager " +
                            "JOIN SanaUser ON MunicipalManager.email = SanaUser.email WHERE name LIKE '%"+patron+"%'",
                    new MunicipalManagerRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<MunicipalManager>();
        }
    }
}
