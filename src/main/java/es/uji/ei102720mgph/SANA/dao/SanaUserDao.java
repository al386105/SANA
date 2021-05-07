package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.ControlStaff;
import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
import es.uji.ei102720mgph.SANA.model.SanaUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SanaUserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public SanaUser getSanaUser(String email){
        try{

            return jdbcTemplate.queryForObject("SELECT * FROM SanaUser WHERE email=?",
                    new SanaUserRowMapper(), email);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

}
