package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.TemporalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TemporalServiceDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addTemporalService(TemporalService temporalService) {
        jdbcTemplate.update(
                "INSERT INTO TemporalService VALUES(?, ?, ?, ?, ?, ?, ?)",
                temporalService.getOpenningDays(), temporalService.getBeginningTime(), temporalService.getEndTime(),
                temporalService.getBeginningDate(), temporalService.getEndDate(), temporalService.getService(),
                temporalService.getNaturalArea());
    }

    public void deleteTemporalService(String service, String naturalArea) {
        //System.out.println("TemporalService Delete Dao: "+service + naturalArea);
        jdbcTemplate.update("DELETE FROM TemporalService WHERE service =? AND naturalArea=?", service, naturalArea);
    }

    public void updateTemporalService(TemporalService temporalService) {
        jdbcTemplate.update("UPDATE TemporalService SET openingDays = ?, beginningTime = ?, endTime = ?, beginningDate = ?, " +
                        "endDate = ? WHERE service =? AND naturalArea = ?",
                temporalService.getOpenningDays(), temporalService.getBeginningTime(), temporalService.getEndTime(),
                temporalService.getBeginningDate(), temporalService.getEndDate(), temporalService.getService(), temporalService.getNaturalArea());
    }

    public TemporalService getTemporalService(String service, String naturalArea) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM TemporalService WHERE service =? AND naturalArea =?",
                    new TemporalServiceRowMapper(), service, naturalArea);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<TemporalService> getTemporalServices() {

        try {
            return jdbcTemplate.query(
                    "SELECT * FROM TemporalService",
                    new TemporalServiceRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<TemporalService>();
        }
    }
}
