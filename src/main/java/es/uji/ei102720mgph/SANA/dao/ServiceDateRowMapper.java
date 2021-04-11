package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.ServiceDate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ServiceDateRowMapper  implements RowMapper<ServiceDate>{
    public ServiceDate mapRow(ResultSet rs, int rowNum) throws SQLException {
        ServiceDate serviceDate = new ServiceDate();
        serviceDate.setId(rs.getString("id"));
        serviceDate.setBeginningDate(rs.getObject("beginningDate", LocalDate.class));
        Date d = rs.getDate("endDate");
        serviceDate.setEndDate(d != null ? d.toLocalDate() : null);
        serviceDate.setService(rs.getString("service"));
        serviceDate.setNaturalArea(rs.getString("naturalArea"));
        return serviceDate;
    }
}
