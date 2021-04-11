package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Municipality;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class MunicipalityRowMapper implements RowMapper<Municipality> {
    public Municipality mapRow(ResultSet rs, int rowNum) throws SQLException {
        Municipality municipality = new Municipality();
        municipality.setName(rs.getString("name"));
        municipality.setDescription(rs.getString("description"));
        municipality.setRegistrationDate(rs.getObject("registrationDate", LocalDate.class));
        return municipality;
    }
}
