package es.uji.ei102720mgph.SANA.dao;


import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
                "INSERT INTO MunicipalManager VALUES(?, ?, ?, ?)",
                manager.getEmail(), manager.getUsername(), manager.getPassword(), manager.getMunicipality());

    }

    public void deleteMunicipalManager(String email) {
        jdbcTemplate.update("DELETE FROM MunicipalManager WHERE email =?", email);
    }

    public void updateMunicipalManager(MunicipalManager manager) {
        jdbcTemplate.update("UPDATE MunicipalManager SET username = ?, password = ?, municipality = ? WHERE email =?",
                manager.getUsername(), manager.getPassword(), manager.getMunicipality(), manager.getEmail());
    }

    public MunicipalManager getMunicipalManager(String email) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM MunicipalManager WHERE email =?",
                    new MunicipalManagerRowMapper(), email);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<MunicipalManager> getMunicipalManagers() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM MunicipalManager",
                    new MunicipalManagerRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<MunicipalManager>();
        }
    }
}
