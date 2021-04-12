package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.PostalCodeMunicipality;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class PostalCodeMunicipalityRowMapper implements RowMapper<PostalCodeMunicipality> {
    public PostalCodeMunicipality mapRow(ResultSet rs, int rowNum) throws SQLException {
        PostalCodeMunicipality postalCode = new PostalCodeMunicipality();
        postalCode.setMunicipality(rs.getString("municipality"));
        postalCode.setPostalCode(rs.getString("postalCode"));
        return postalCode;
    }
}
