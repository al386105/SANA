package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Municipality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MunicipalityDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addMunicipality(Municipality muni) {
        jdbcTemplate.update(
                "INSERT INTO Municipality VALUES(?, ?, ?)",
                muni.getName(), muni.getDescription(), muni.getRegistrationDate());
    }

    public void deleteMunicipality(String name) {
        jdbcTemplate.update("DELETE FROM Municipality WHERE name =?", name);
    }

    public void updateMunicipality(Municipality muni) {
        jdbcTemplate.update("UPDATE Municipality SET description = ?, registrationDate = ? WHERE name =?",
                muni.getDescription(), muni.getRegistrationDate(), muni.getName());
    }

    public Municipality getMunicipality(String name) {
        try {
            return (Municipality) jdbcTemplate.queryForObject("SELECT * FROM Municipality WHERE name =?", new MunicipalityRowMapper(), name);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Municipality> getMunicipalities() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Municipality",
                    new MunicipalityRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Municipality>();
        }
    }

}
