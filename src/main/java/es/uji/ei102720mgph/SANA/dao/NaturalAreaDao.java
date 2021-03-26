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
                naturalArea.getName(), naturalArea.getTypeOfAccess(), naturalArea.getGeographicalLocation(),
                naturalArea.getTypeOfArea(), naturalArea.getLength(), naturalArea.getWidth(),
                naturalArea.getPhysicalCharacteristics(), naturalArea.getDescription(), naturalArea.getOrientation(),
                naturalArea.getRestrictionTimePeriod(), naturalArea.getOccupancyRate(), naturalArea.getMunicipality());
    }

    public void deleteNaturalArea(NaturalArea naturalArea) {
        jdbcTemplate.update("DELETE FROM NaturalArea WHERE name = ?",
                naturalArea.getName());
    }

    public void updateNaturalArea(NaturalArea naturalArea) {
        jdbcTemplate.update("UPDATE NaturalArea " +
                        "SET typeOfAccess = ?, geographicalLocation = ?, typeOfArea = ?, " +
                        "length = ?, width = ?, physicalCharacterisitics = ?," +
                        "description = ?, orientation = ?, restrictionTimePeriod = ?," +
                        "occupancyRate = ?, municipality = ?" +
                        "WHERE name = ?",
                naturalArea.getTypeOfAccess(), naturalArea.getGeographicalLocation(),
                naturalArea.getTypeOfArea(), naturalArea.getLength(), naturalArea.getWidth(),
                naturalArea.getPhysicalCharacteristics(), naturalArea.getDescription(), naturalArea.getOrientation(),
                naturalArea.getRestrictionTimePeriod(), naturalArea.getOccupancyRate(), naturalArea.getMunicipality(),
                naturalArea.getName());
    }

    public NaturalArea getNaturalArea(String name) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM NaturalArea WHERE nom = ?",
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

}
