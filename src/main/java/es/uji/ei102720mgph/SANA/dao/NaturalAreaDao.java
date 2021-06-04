package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.NaturalArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class NaturalAreaDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addNaturalArea(NaturalArea naturalArea) {
        jdbcTemplate.update("INSERT INTO NaturalArea VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                naturalArea.getName(), naturalArea.getTypeOfAccess().toString(), naturalArea.getGeographicalLocation(),
                naturalArea.getTypeOfArea().toString(), naturalArea.getLength(), naturalArea.getWidth(),
                naturalArea.getPhysicalCharacteristics(), naturalArea.getDescription(), naturalArea.getOrientation().toString(),
                naturalArea.getRestrictionTimePeriod(), naturalArea.getOccupancyRate(), naturalArea.getMunicipality());
    }

    public void updateNaturalArea(NaturalArea naturalArea) {
        jdbcTemplate.update("UPDATE NaturalArea " +
                        "SET typeOfAccess = ?, geographicalLocation = ?, typeOfArea = ?, " +
                        "length = ?, width = ?, physicalCharacteristics = ?, " +
                        "description = ?, orientation = ?, restrictionTimePeriod = ?, " +
                        "occupancyRate = ?, municipality = ? " +
                        "WHERE name = ?",
                naturalArea.getTypeOfAccess().toString(), naturalArea.getGeographicalLocation(),
                naturalArea.getTypeOfArea().toString(), naturalArea.getLength(), naturalArea.getWidth(),
                naturalArea.getPhysicalCharacteristics(), naturalArea.getDescription(), naturalArea.getOrientation().toString(),
                naturalArea.getRestrictionTimePeriod(), naturalArea.getOccupancyRate(), naturalArea.getMunicipality(),
                naturalArea.getName());
    }

    public NaturalArea getNaturalArea(String name) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM NaturalArea WHERE name = ?",
                    new NaturalAreaRowMapper(),
                    name);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<NaturalArea> getNaturalAreas() {
        try {
            return jdbcTemplate.query("SELECT * FROM NaturalArea",
                    new NaturalAreaRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<NaturalArea>();
        }
    }

    public List<NaturalArea> getRestrictedNaturalAreas() {
        try {
            return jdbcTemplate.query("SELECT * FROM NaturalArea " +
                            "WHERE typeOfAccess = 'restricted'",
                    new NaturalAreaRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<NaturalArea>();
        }
    }

    public List<NaturalArea> getRestrictedAndNonNaturalAreas() {
        try {
            return jdbcTemplate.query("SELECT * FROM NaturalArea " +
                            "WHERE typeOfAccess != 'closed'",
                    new NaturalAreaRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<NaturalArea>();
        }
    }

    public  List<NaturalArea> getNaturalAreaSearch(String patron) {
        try {
            return jdbcTemplate.query("SELECT * FROM NaturalArea WHERE UPPER(name) LIKE '%"+patron.toUpperCase()+"%'",
                    new NaturalAreaRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<NaturalArea>();
        }
    }

    public NaturalArea getNaturalAreaOfZone(String id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM naturalArea JOIN zone ON naturalArea.name = zone.naturalArea " +
                            "WHERE zone.id = ?",
                    new NaturalAreaRowMapper(), id);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }
}
