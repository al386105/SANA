package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.PostalCodeMunicipality;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class PostalCodeMunicipalityRowMapper implements RowMapper {
    @Override
    public PostalCodeMunicipality mapRow(ResultSet rs, int rowNum) throws SQLException {
        PostalCodeMunicipality pc = new PostalCodeMunicipality();
        pc.setMunicipality(rs.getString("municipality"));
        pc.setPostalCode(rs.getString("postalCode"));
        return pc;
    }
}
