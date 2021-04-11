package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class MunicipalManagerRowMapper implements RowMapper<MunicipalManager> {
    public MunicipalManager mapRow(ResultSet rs, int rowNum) throws SQLException {
        MunicipalManager municipalManager = new MunicipalManager();
        municipalManager.setEmail(rs.getString("email"));
        municipalManager.setUsername(rs.getString("username"));
        municipalManager.setPassword(rs.getString("password"));
        municipalManager.setMunicipality(rs.getString("municipality"));
        return municipalManager;
    }
}
