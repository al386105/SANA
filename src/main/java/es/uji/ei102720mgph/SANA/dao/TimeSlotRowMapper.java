package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.TimeSlot;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public final class TimeSlotRowMapper implements RowMapper {
    @Override
    public TimeSlot mapRow(ResultSet rs, int rowNum) throws SQLException {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(rs.getString("id"));
        timeSlot.setBeginningDate(rs.getObject("beginningDate", LocalDate.class));
        timeSlot.setEndDate(rs.getObject("endDate", LocalDate.class));
        timeSlot.setBeginningTime(rs.getObject("beginningTime", LocalTime.class));
        timeSlot.setEndTime(rs.getObject("endTime", LocalTime.class));
        timeSlot.setNaturalArea(rs.getString("naturalArea"));
        return timeSlot;
    }
}
