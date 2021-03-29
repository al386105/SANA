package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Municipality;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class MunicipalityRowMapper implements RowMapper {
    @Override
    public Municipality mapRow(ResultSet rs, int rowNum) throws SQLException {
        Municipality muni = new Municipality();
        muni.setName(rs.getString("name"));
        muni.setDescription(rs.getString("description"));
        muni.setRegistrationDate(rs.getObject("registrationDate", LocalDate.class));
        return muni;
    }
}
