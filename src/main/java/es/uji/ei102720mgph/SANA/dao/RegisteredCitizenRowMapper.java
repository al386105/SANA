package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.TypeOfArea;
import es.uji.ei102720mgph.SANA.enums.TypeOfUser;
import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class RegisteredCitizenRowMapper implements RowMapper<RegisteredCitizen> {
    public RegisteredCitizen mapRow(ResultSet rs, int rowNum) throws SQLException {
        RegisteredCitizen registeredCitizen = new RegisteredCitizen();
        registeredCitizen.setEmail(rs.getString("email"));
        registeredCitizen.setName(rs.getString("name"));
        registeredCitizen.setSurname(rs.getString("surname"));
        registeredCitizen.setDateOfBirth(rs.getObject("dateOfBirth", LocalDate.class));
        registeredCitizen.setRegistrationDate(rs.getObject("registrationDate", LocalDate.class));
        registeredCitizen.setUsername(rs.getString("citizenCode"));
        Date d = rs.getDate("leavingDate");
        TypeOfUser typeOfUser = TypeOfUser.valueOf(rs.getString("typeOfUser"));
        registeredCitizen.setTypeOfUser(typeOfUser);
        registeredCitizen.setLeavingDate(d != null ? d.toLocalDate() : null);
        registeredCitizen.setIdNumber(rs.getString("idNumber"));
        registeredCitizen.setMobilePhoneNumber(rs.getString("mobilePhoneNumber"));
        registeredCitizen.setPin(rs.getInt("pin"));
        registeredCitizen.setAddressId(rs.getString("addressId"));
        return registeredCitizen;
    }
}
