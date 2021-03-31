package es.uji.ei102720mgph.SANA.dao;


import es.uji.ei102720mgph.SANA.model.ServiceDate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceDateRowMapper  implements RowMapper<ServiceDate>{
    public ServiceDate mapRow(ResultSet rs, int rowNum) throws SQLException {
        ServiceDate serviceDate = new ServiceDate();
        serviceDate.setId(rs.getString("id"));
        serviceDate.setBeginningDate(rs.getDate("beginningDate"));
        serviceDate.setEndDate(rs.getDate("endDate"));
        serviceDate.setService(rs.getString("service"));
        serviceDate.setNaturalArea(rs.getString("naturalArea"));
        return serviceDate;
    }
}
