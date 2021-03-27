package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class MunicipalManagerRowMapper implements RowMapper<MunicipalManager> {
    @Override
    public MunicipalManager mapRow(ResultSet rs, int rowNum) throws SQLException {
        MunicipalManager manager = new MunicipalManager();
        manager.setEmail(rs.getString("email"));
        manager.setUsername(rs.getString("username"));
        manager.setPassword(rs.getString("password"));
        manager.setMunicipality(rs.getString("municipality"));
        return manager;
    }
}
