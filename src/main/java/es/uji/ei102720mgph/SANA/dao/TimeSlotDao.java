package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Address;
import es.uji.ei102720mgph.SANA.model.Reservation;
import es.uji.ei102720mgph.SANA.model.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

@Repository
public class TimeSlotDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addTimeSlot(TimeSlot ts) {
        boolean excepcion;
        Formatter fmt;
        do {
            try {
                fmt = new Formatter();
                jdbcTemplate.update(
                        "INSERT INTO TimeSlot VALUES(?, ?, ?, ?, ?, ?)",
                        "" + fmt.format("%06d", TimeSlot.getContador()), ts.getBeginningDate(), ts.getEndDate(),
                        ts.getBeginningTime(), ts.getEndTime(), ts.getNaturalArea());
                excepcion = false;
            } catch (DuplicateKeyException e) {
                excepcion = true;
            } finally {
                TimeSlot.incrementaContador();
            }
        } while (excepcion);
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

    public List<TimeSlot> getTimeSlotNaturalArea(String naturalArea) {
        try {
            return jdbcTemplate.query("SELECT * FROM TimeSlot " +
                            "WHERE naturalArea = ?",
                    new TimeSlotRowMapper(),
                    naturalArea);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<TimeSlot>();
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
