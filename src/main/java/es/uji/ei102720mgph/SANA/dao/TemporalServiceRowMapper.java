package es.uji.ei102720mgph.SANA.dao;


import es.uji.ei102720mgph.SANA.model.TemporalService;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TemporalServiceRowMapper implements RowMapper<TemporalService> {
    public TemporalService mapRow(ResultSet rs, int rowNum) throws SQLException {
        TemporalService temporalService = new TemporalService();
        temporalService.setOpenningDays(rs.getInt("openingdays"));
        temporalService.setBeginningTime(rs.getTime("beginningtime"));
        temporalService.setEndTime(rs.getTime("endtime"));
        temporalService.setBeginningDate(rs.getDate("beginningdate"));
        temporalService.setEndDate(rs.getDate("enddate"));
        temporalService.setService(rs.getString("service"));
<<<<<<< Updated upstream
        temporalService.setNaturalArea(rs.getString("naturalarea"));

=======
        temporalService.setNaturalArea(rs.getString("naturalArea"));
>>>>>>> Stashed changes
        return temporalService;
    }
}
