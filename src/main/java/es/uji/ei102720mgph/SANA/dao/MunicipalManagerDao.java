package es.uji.ei102720mgph.SANA.dao;


import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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
}
