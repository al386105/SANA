package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.TemporalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

@Repository
public class TemporalServiceDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addTemporalService(TemporalService temporalService) {
        boolean excepcion;
        Formatter fmt;
        do {
            try {
                fmt = new Formatter();
                jdbcTemplate.update(
                        "INSERT INTO TemporalService VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                        "" + fmt.format("%06d", TemporalService.getContador()), temporalService.getOpeningDays(),
                        temporalService.getBeginningTime(), temporalService.getEndTime(),
                        temporalService.getBeginningDate(), temporalService.getEndDate(), temporalService.getService(),
                        temporalService.getNaturalArea());
                excepcion = false;
            } catch (DuplicateKeyException e) {
                excepcion = true;
            } finally {
                TemporalService.incrementaContador();
            }
        } while (excepcion);
    }

    public void updateTemporalService(TemporalService temporalService) {
        jdbcTemplate.update("UPDATE TemporalService SET openingDays = ?, beginningTime = ?, endTime = ?, beginningDate = ?, " +
                        "endDate = ?, service = ?, naturalArea = ? WHERE id = ?",
                temporalService.getOpeningDays(), temporalService.getBeginningTime(), temporalService.getEndTime(),
                temporalService.getBeginningDate(), temporalService.getEndDate(), temporalService.getService(),
                temporalService.getNaturalArea(), temporalService.getId());
    }

    public TemporalService getTemporalService(String id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM TemporalService WHERE id =?",
                    new TemporalServiceRowMapper(), id);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<TemporalService> getTemporalServicesOfNaturalArea(String naturalArea) {
        try {
            return jdbcTemplate.query("SELECT * FROM TemporalService WHERE naturalArea = ?",
                    new TemporalServiceRowMapper(),
                    naturalArea);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<TemporalService>();
        }
    }
}
