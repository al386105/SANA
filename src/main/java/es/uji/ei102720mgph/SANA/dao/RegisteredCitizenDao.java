package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Address;
import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RegisteredCitizenDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addRegisteredCitizen(RegisteredCitizen registeredCitizen){
        jdbcTemplate.update("INSERT INTO RegisteredCitizen VALUES(?, ?, ?, ?, ?, ?)",
                registeredCitizen.getIdNumber(), registeredCitizen.getEmail(),
                registeredCitizen.getMobilePhoneNumber(), registeredCitizen.getCitizenCode(),
                registeredCitizen.getPin(),  registeredCitizen.getAddressId());
    }

    public void deleteRegisteredCitizen(String idNumber){
        jdbcTemplate.update("DELETE FROM RegisteredCitizen WHERE idNumber =?", idNumber);
    }

    public void updateRegisteredCitizen(RegisteredCitizen registeredCitizen){
        jdbcTemplate.update("UPDATE RegisteredCitizen SET email = ?, mobilePhoneNumber = ?, citizenCode = ?," +
                        "pin = ?, addressId = ? WHERE idNumber =?",
                registeredCitizen.getEmail(), registeredCitizen.getMobilePhoneNumber(), registeredCitizen.getCitizenCode(),
                registeredCitizen.getPin(), registeredCitizen.getAddressId(), registeredCitizen.getIdNumber());
    }

    public  RegisteredCitizen getRegisteredCitizen(String email){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM RegsiteredCitizen WHERE email =?",
                    new RegisteredCitizenRowMapper(), email);
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
            return new ArrayList<RegisteredCitizen>();
        }
    }

}
