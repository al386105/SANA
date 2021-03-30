package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.PostalCodeMunicipality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostalCodeMunicipalityDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addPostalCode(PostalCodeMunicipality pc) {
        jdbcTemplate.update(
                "INSERT INTO PostalCodeMunicipality VALUES(?, ?)",
                pc.getMunicipality(), pc.getPostalCode());
    }

    public void deletePostalCode(String municipality, String postalCode) {
        jdbcTemplate.update("DELETE FROM PostalCodeMunicipality WHERE municipality =? AND postalCode = ?", municipality, postalCode);
    }

    public List<PostalCodeMunicipality> getPostalCodeOfMuni(String municipality) {
        try {
            return jdbcTemplate.query("SELECT * FROM PostalCodeMunicipality WHERE municipality =?", new PostalCodeMunicipalityRowMapper(), municipality);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<PostalCodeMunicipality> getPostalCodes() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM PostalCodeMunicipality",
                    new PostalCodeMunicipalityRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<PostalCodeMunicipality>();
        }
    }
}
