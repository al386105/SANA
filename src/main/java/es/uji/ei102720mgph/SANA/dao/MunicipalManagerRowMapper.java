package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class MunicipalManagerRowMapper implements RowMapper<MunicipalManager> {
    public MunicipalManager mapRow(ResultSet rs, int rowNum) throws SQLException {
        MunicipalManager municipalManager = new MunicipalManager();
        municipalManager.setEmail(rs.getString("email"));
        municipalManager.setName(rs.getString("name"));
        municipalManager.setSurname(rs.getString("surname"));
        municipalManager.setDateOfBirth(rs.getObject("dateOfBirth", LocalDate.class));
        municipalManager.setRegistrationDate(rs.getObject("registrationDate", LocalDate.class));
        Date d = rs.getDate("leavingDate");
        municipalManager.setLeavingDate(d != null ? d.toLocalDate() : null);
        municipalManager.setUsername(rs.getString("username"));
        municipalManager.setPassword(rs.getString("password"));
        municipalManager.setMunicipality(rs.getString("municipality"));
        return municipalManager;
    }
}
