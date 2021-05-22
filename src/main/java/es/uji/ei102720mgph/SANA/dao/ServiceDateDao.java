package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.ServiceDate;
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
public class ServiceDateDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addServiceDate(ServiceDate serviceDate) {
        boolean excepcion;
        Formatter fmt;
        do {
            try {
                fmt = new Formatter();
                jdbcTemplate.update(
                        "INSERT INTO ServiceDate VALUES(?, ?, ?, ?, ?)",
                        "" + fmt.format("%06d", ServiceDate.getContador()), serviceDate.getBeginningDate(),
                        serviceDate.getEndDate(), serviceDate.getService(), serviceDate.getNaturalArea());
                excepcion = false;
            } catch (DuplicateKeyException e) {
                excepcion = true;
            } finally {
                ServiceDate.incrementaContador();
            }
        } while (excepcion);
    }

    public void updateServiceDate(ServiceDate serviceDate) {
        jdbcTemplate.update("UPDATE ServiceDate SET beginningDate = ?, endDate = ?, service = ?, naturalArea = ?" +
                        " WHERE id = ?",
                serviceDate.getBeginningDate(), serviceDate.getEndDate(), serviceDate.getService(), serviceDate.getNaturalArea(),
                serviceDate.getId());
    }

    public List<ServiceDate> getServiceDatesOfNaturalArea(String naturalArea) {
        try {
            return jdbcTemplate.query("SELECT * FROM ServiceDate WHERE naturalArea = ?",
                    new ServiceDateRowMapper(),
                    naturalArea);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<ServiceDate>();
        }
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
}
