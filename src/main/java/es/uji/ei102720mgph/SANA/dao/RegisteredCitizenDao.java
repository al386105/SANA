package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.TypeOfUser;
import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
import es.uji.ei102720mgph.SANA.model.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Formatter;

@Repository
public class RegisteredCitizenDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int addRegisteredCitizen(RegisteredCitizen registeredCitizen){
        jdbcTemplate.update("INSERT INTO SanaUser VALUES(?, ?, ?, ?, ?, ?, ?)",
                registeredCitizen.getEmail(), registeredCitizen.getName(), registeredCitizen.getSurname(),
                registeredCitizen.getDateOfBirth(), LocalDate.now(), null, TypeOfUser.registeredCitizen.name());

        boolean excepcion;
        Formatter fmt;
        do {
            try {
                fmt = new Formatter();
                jdbcTemplate.update("INSERT INTO RegisteredCitizen VALUES(?, ?, ?, ?, ?, ?)",
                        registeredCitizen.getEmail(), registeredCitizen.getIdNumber(),
                        registeredCitizen.getMobilePhoneNumber(), "ci" + fmt.format("%04d", RegisteredCitizen.getContador()),
                        registeredCitizen.getPin(), registeredCitizen.getAddressId()
                );
                excepcion = false;
            } catch (DuplicateKeyException e) {
                excepcion = true;
            } finally {
                RegisteredCitizen.incrementaContador();
            }
        } while (excepcion);
        return RegisteredCitizen.getContador()-1;
    }

    public void updateRegisteredCitizen(RegisteredCitizen registeredCitizen){
        jdbcTemplate.update("UPDATE RegisteredCitizen SET idNumber = ?, mobilePhoneNumber = ?, citizenCode = ?, " +
                        "pin = ?, addressId = ? WHERE email =?",
                registeredCitizen.getIdNumber(), registeredCitizen.getMobilePhoneNumber(), registeredCitizen.getCitizenCode(),
                registeredCitizen.getPin(), registeredCitizen.getAddressId(), registeredCitizen.getEmail());
        jdbcTemplate.update("UPDATE SanaUser SET name = ?, surname = ?, dateOfBirth = ?, " +
                        "registrationDate = ?, leavingDate = ?, typeOfUser = ? " +
                        "WHERE email =?",
                registeredCitizen.getName(), registeredCitizen.getSurname(), registeredCitizen.getDateOfBirth(),
                registeredCitizen.getRegistrationDate(), registeredCitizen.getLeavingDate(),
                TypeOfUser.registeredCitizen.name(), registeredCitizen.getEmail());
    }

    public RegisteredCitizen getRegisteredCitizen(String email){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM RegisteredCitizen " +
                            "JOIN SanaUser ON RegisteredCitizen.email = SanaUser.email " +
                            "WHERE SanaUser.email = ? ",
                    new RegisteredCitizenRowMapper(), email);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public RegisteredCitizen getRegisteredCitizenNIE(String idNumber){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM RegisteredCitizen " +
                            "JOIN SanaUser ON RegisteredCitizen.email = SanaUser.email " +
                            "WHERE RegisteredCitizen.idNumber = ? ",
                    new RegisteredCitizenRowMapper(), idNumber);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public RegisteredCitizen getRegisteredCitizenTelf(String telf){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM RegisteredCitizen " +
                            "JOIN SanaUser ON RegisteredCitizen.email = SanaUser.email " +
                            "WHERE RegisteredCitizen.mobilePhoneNumber = ?",
                    new RegisteredCitizenRowMapper(), telf);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public RegisteredCitizen getRegisteredCitizenCitizenCode(String citizenCode){
        try{
            return jdbcTemplate.queryForObject("SELECT * FROM RegisteredCitizen " +
                            "JOIN SanaUser ON RegisteredCitizen.email = SanaUser.email " +
                            "WHERE RegisteredCitizen.citizenCode = ? ",
                    new RegisteredCitizenRowMapper(), citizenCode);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
