package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.TemporalService;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class TemporalServiceRowMapper implements RowMapper<TemporalService> {
    public TemporalService mapRow(ResultSet rs, int rowNum) throws SQLException {
        TemporalService temporalService = new TemporalService();
        temporalService.setOpenningDays(rs.getInt("openingDays"));
        temporalService.setBeginningTime(rs.getObject("beginningTime", LocalTime.class));
        temporalService.setEndTime(rs.getObject("endTime", LocalTime.class));
        temporalService.setBeginningDate(rs.getObject("beginningDate", LocalDate.class));
        temporalService.setEndDate(rs.getObject("endDate", LocalDate.class));
        temporalService.setService(rs.getString("service"));
        temporalService.setNaturalArea(rs.getString("naturalArea"));
        return temporalService;
    }
}
