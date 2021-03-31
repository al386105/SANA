package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.ServiceDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ServiceDateDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public void addServiceDate(ServiceDate serviceDate) {
        jdbcTemplate.update(
                "INSERT INTO ServiceDate VALUES(?, ?, ?, ?, ?)",
                serviceDate.getId(), serviceDate.getBeginningDate(), serviceDate.getEndDate(),
                serviceDate.getService(), serviceDate.getNaturalArea());
    }


    public void deleteServiceDate(String id) {
        jdbcTemplate.update("DELETE FROM ServiceDate WHERE id =?", id);
    }


    public void updateServiceDate(ServiceDate serviceDate) {
        jdbcTemplate.update("UPDATE ServiceDate SET beginningDate = ?, endDate = ?, service = ?, naturalArea = ?," +
                        " WHERE id =?",
                serviceDate.getBeginningDate(), serviceDate.getEndDate(), serviceDate.getService(), serviceDate.getNaturalArea(),
                serviceDate.getId());
    }


    public ServiceDate getServiceDate(String id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM ServiceDate WHERE id =?",
                    new ServiceDateRowMapper(), id);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<ServiceDate> getServiceDates() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM ServiceDate",
                    new ServiceDateRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<ServiceDate>();
        }
    }
}
