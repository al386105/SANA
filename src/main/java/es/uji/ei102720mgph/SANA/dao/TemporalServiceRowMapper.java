package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Address;
import es.uji.ei102720mgph.SANA.model.TemporalService;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TemporalServiceRowMapper implements RowMapper<TemporalService> {
    public TemporalService mapRow(ResultSet rs, int rowNum) throws SQLException {
        TemporalService temporalService = new TemporalService();
        temporalService.setOpenningDays(rs.getInt("openingDays"));
        temporalService.setBeginningTime(rs.getTime("beginningTime"));
        temporalService.setEndTime(rs.getTime("endTime"));
        temporalService.setBeginningDate(rs.getDate("beginningDate"));
        temporalService.setEndDate(rs.getDate("endDate"));
        temporalService.setService(rs.getString("service"));
        temporalService.setNaturalArea(rs.getString("naturalArea"));

        return temporalService;
    }
}
