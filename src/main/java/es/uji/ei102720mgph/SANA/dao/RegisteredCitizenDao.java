package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.TypeOfUser;
import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.time.LocalDate;
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
        jdbcTemplate.update("INSERT INTO Receiver VALUES(?, ?, ?, ?, ?, ?, ?)",
                registeredCitizen.getEmail(), registeredCitizen.getName(), registeredCitizen.getSurname(), registeredCitizen.getDateOfBirth(),
                LocalDate.now(), null, TypeOfUser.registeredCitizen);
        jdbcTemplate.update("INSERT INTO RegisteredCitizen VALUES(?, ?, ?, ?, ?, ?)",
                registeredCitizen.getEmail(), registeredCitizen.getIdNumber(),
                registeredCitizen.getMobilePhoneNumber(), registeredCitizen.getCitizenCode(),
                registeredCitizen.getPin(),  registeredCitizen.getAddressId());
    }

    public void deleteRegisteredCitizen(String email){
        jdbcTemplate.update("DELETE FROM RegisteredCitizen WHERE email =?", email);
        jdbcTemplate.update("DELETE FROM Receiver WHERE email =?", email);
    }

    public void updateRegisteredCitizen(RegisteredCitizen registeredCitizen){
        jdbcTemplate.update("UPDATE RegisteredCitizen SET idNumber = ?, mobilePhoneNumber = ?, citizenCode = ?, " +
                        "pin = ?, addressId = ? WHERE email =?",
                registeredCitizen.getIdNumber(), registeredCitizen.getMobilePhoneNumber(), registeredCitizen.getCitizenCode(),
                registeredCitizen.getPin(), registeredCitizen.getAddressId(), registeredCitizen.getEmail());
        jdbcTemplate.update("UPDATE Receiver SET name = ?, surname = ?, dateOfBirth = ?, " +
                        "registrationDate = ?, leavingDate = ?, typeOfUser = ? " +
                        "WHERE email =?",
                registeredCitizen.getName(), registeredCitizen.getSurname(), registeredCitizen.getDateOfBirth(),
                registeredCitizen.getRegistrationDate(), registeredCitizen.getLeavingDate(), registeredCitizen.getTypeOfUser(),
                registeredCitizen.getEmail());
    }

    public  RegisteredCitizen getRegisteredCitizen(String email){
        try{
            System.out.println(email);
            return jdbcTemplate.queryForObject("SELECT * FROM RegisteredCitizen " +
                            "JOIN Receiver ON RegisteredCitizen.email = Receiver.email " +
                            "WHERE Receiver.email = ? ",
                    new RegisteredCitizenRowMapper(), email);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<RegisteredCitizen> getRegisteredCitizens(){
        try{
            return jdbcTemplate.query("SELECT * FROM RegisteredCitizen " +
                    "JOIN Receiver ON RegisteredCitizen.email = Receiver.email",
                    new RegisteredCitizenRowMapper());
        }
        catch (EmptyResultDataAccessException e){
            return new ArrayList<RegisteredCitizen>();
        }
    }

}
