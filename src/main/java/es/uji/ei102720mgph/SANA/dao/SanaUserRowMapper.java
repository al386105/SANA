package es.uji.ei102720mgph.SANA.dao;


import es.uji.ei102720mgph.SANA.enums.TypeOfUser;
import es.uji.ei102720mgph.SANA.model.SanaUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class SanaUserRowMapper implements RowMapper<SanaUser> {
    public SanaUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        SanaUser sanaUser = new SanaUser();
        sanaUser.setEmail(rs.getString("email"));
        sanaUser.setName(rs.getString("name"));
        sanaUser.setSurname(rs.getString("surname"));
        sanaUser.setDateOfBirth(rs.getObject("dateOfBirth", LocalDate.class));
        sanaUser.setRegistrationDate(rs.getObject("registrationDate", LocalDate.class));
        Date d = rs.getDate("leavingDate");
        sanaUser.setLeavingDate(d != null ? d.toLocalDate() : null);
        TypeOfUser typeOfUser = TypeOfUser.valueOf(rs.getString("typeOfUser"));
        sanaUser.setTypeOfUser(typeOfUser);
        return sanaUser;

    }
}
