package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TimeSlotDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addTimeSlot(TimeSlot ts) {
        jdbcTemplate.update(
                "INSERT INTO TimeSlot VALUES(?, ?, ?, ?, ?, ?)",
                ts.getId(), ts.getBeginningDate(), ts.getEndDate(), ts.getBeginningTime(), ts.getEndTime(), ts.getNaturalArea());
    }

    public void deleteTimeSlot(String id) {
        jdbcTemplate.update("DELETE FROM TimeSlot WHERE id =?", id);
    }

    public void updateTimeSlot(TimeSlot ts) {
        jdbcTemplate.update("UPDATE TimeSlot SET beginningDate = ?, endDate = ?, beginningTime = ?, endTime = ?, naturalArea = ? WHERE id =?",
                ts.getBeginningDate(), ts.getEndDate(), ts.getBeginningTime(), ts.getEndTime(), ts.getNaturalArea(), ts.getId());
    }

    public TimeSlot getTimeSlot(String id) {
        try {
            return (TimeSlot) jdbcTemplate.queryForObject("SELECT * FROM TimeSlot WHERE id =?", new TimeSlotRowMapper(), id);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<TimeSlot> getTimeSlots() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM TimeSlot",
                    new TimeSlotRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<TimeSlot>();
        }
    }
}
