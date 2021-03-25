package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class RegisteredCitizenDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addRegisteredCitizen(RegisteredCitizen registeredCitizen){
        jdbcTemplate.update("INSERT INTO RegisteredCitizen VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                registeredCitizen.getName(), registeredCitizen.getSurname(), registeredCitizen.getId(), registeredCitizen.getEmail(),
                registeredCitizen.getMobilePhoneNumber(), registeredCitizen.getAddres(), registeredCitizen.getDateOfBirth(), registeredCitizen.getCitizenCode(),
                registeredCitizen.getPin(), registeredCitizen.getRegistrationDate(), registeredCitizen.getIdAddres());
    }

    public void deleteRegisteredCitizen(RegisteredCitizen registeredCitizen){
        jdbcTemplate.update("DELETE FROM RegisteredCitizen WHERE id ='"+ registeredCitizen.getId()+"'");
    }

    public void updateRegisteredCitizen(RegisteredCitizen registeredCitizen){
        jdbcTemplate.update("UPDATE RegisteredCitizen SET n");
    }

    public  RegisteredCitizen getRegisteredCitizen(String id){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM RegisteredCitizen WHERE id ='" + id + "'", new RegisteredCitizenRowMapper());
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<RegisteredCitizen> getRegisteredCitizens(){
        try{
            return jdbcTemplate.query("SELECT * FROM RegisteredCitizen", new RegisteredCitizenRowMapper());
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

}
