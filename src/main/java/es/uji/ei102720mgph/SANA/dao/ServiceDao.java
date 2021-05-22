package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ServiceDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el address a la base de dades */
    public void addService(Service service) {
        jdbcTemplate.update(
                "INSERT INTO Service VALUES(?, ?, ?, ?)", service.getNameOfService(),
                service.getTemporality().name(), service.getDescription(), service.getHiringPlace());
    }

    /* Esborra el address de la base de dades */
    public void deleteService(String nameOfService) {
        jdbcTemplate.update("DELETE FROM Service WHERE nameOfService =?", nameOfService);
    }

    /* Actualitza els atributs del address (excepte el id, que és la clau primària) */
    public void updateService(Service service) {
        jdbcTemplate.update("UPDATE Service SET temporality = ?, description = ?, hiringPlace = ?  WHERE nameOfService = ?",
                service.getTemporality().name(), service.getDescription(), service.getHiringPlace(),
                service.getNameOfService());
    }

    /* Obté el address amb el id donat. Torna null si no existeix. */
    public Service getService(String nameOfService) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM Service WHERE nameOfService =?",
                    new ServiceRowMapper(), nameOfService);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté tots els addresss. Torna una llista buida si no n'hi ha cap. */
    public List<Service> getServices() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Service",
                    new ServiceRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Service>();
        }
    }

    public List<Service> getTemporalServices() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Service WHERE temporality='temporal'",
                    new ServiceRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Service>();
        }
    }

    public List<Service> getServiceDatesNotInNaturalArea(String naturalArea) {
        try {
            return jdbcTemplate.query("SELECT * FROM Service WHERE temporality='fixed' AND nameOfService NOT IN " +
                            "(SELECT service FROM ServiceDate WHERE naturalArea = ? AND endDate IS NULL)",
                    new ServiceRowMapper(),
                    naturalArea);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Service>();
        }
    }
}
