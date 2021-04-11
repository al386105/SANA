package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.Temporality;
import es.uji.ei102720mgph.SANA.model.Service;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ServiceRowMapper implements  RowMapper<Service> {
    public Service mapRow(ResultSet rs, int rowNum) throws SQLException {
        Service service = new Service();
        service.setNameOfService(rs.getString("nameOfService"));
        Temporality t = Temporality.valueOf(rs.getString("temporality"));
        service.setTemporality(t);
        service.setDescription(rs.getString("description"));
        service.setHiringPlace(rs.getString("hiringPlace"));
        return service;
    }
}

