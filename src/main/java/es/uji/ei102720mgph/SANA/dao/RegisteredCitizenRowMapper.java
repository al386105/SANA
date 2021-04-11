package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;


import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class RegisteredCitizenRowMapper implements RowMapper<RegisteredCitizen> {
    public RegisteredCitizen mapRow(ResultSet rs, int rowNum) throws SQLException {
        RegisteredCitizen registeredCitizen = new RegisteredCitizen();
        registeredCitizen.setEmail(rs.getString("email"));
        registeredCitizen.setIdNumber(rs.getString("idNumber"));
        registeredCitizen.setMobilePhoneNumber(rs.getString("mobilePhoneNumber"));
        registeredCitizen.setCitizenCode(rs.getString("citizenCode"));
        registeredCitizen.setPin(rs.getInt("pin"));
        registeredCitizen.setAddressId(rs.getString("addressId"));
        return registeredCitizen;

    }
}
