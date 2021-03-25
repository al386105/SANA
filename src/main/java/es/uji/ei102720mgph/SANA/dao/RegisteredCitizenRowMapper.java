package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;


import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class RegisteredCitizenRowMapper  implements RowMapper<RegisteredCitizen> {

    public RegisteredCitizen mapRow(ResultSet rs, int rowNum) throws SQLException {
        RegisteredCitizen registeredCitizen = new RegisteredCitizen();
        registeredCitizen.setName(rs.getString("name"));
        registeredCitizen.setSurname(rs.getString("surname"));
        registeredCitizen.setID(rs.getString("id"));
        registeredCitizen.setEmail(rs.getString("email"));
        registeredCitizen.setMobilePhoneNumber(rs.getString("mobilePhoneNumber"));
        registeredCitizen.setAddres(rs.getString("address"));
        registeredCitizen.setDateOfBirth(rs.getDate("dateOfBirth"));
        registeredCitizen.setCitizenCode(rs.getString("citizenCode"));
        registeredCitizen.setPin(rs.getInt("pin"));
        registeredCitizen.setRegistrationDate(rs.getDate("registrationDate"));
        registeredCitizen.setIdAddres(rs.getString("idAddress"));
        return registeredCitizen;

    }



}
