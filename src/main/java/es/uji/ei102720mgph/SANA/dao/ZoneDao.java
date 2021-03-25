package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ZoneDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /* Afegeix el zone a la base de dades */
    public void addZone(Zone zone) {
        jdbcTemplate.update(
                "INSERT INTO Zone VALUES(?, ?, ?, ?, ?, ?)",
                zone.getId(), zone.getZoneNumber(), zone.getLetter(), zone.getMaximumCapacity(),
                zone.getCreationDate(), zone.getNaturalArea());
    }

    /* Esborra el zone de la base de dades */
    public void deleteZone(String id) {
        jdbcTemplate.update("DELETE FROM Zone WHERE id =?", id);
    }

    /* Actualitza els atributs del zone */
    public void updateZone(Zone zone) {
        jdbcTemplate.update("UPDATE Zone SET zoneNumber = ?, letter = ?, maximumCapacity = ?, creationDate = ?, " +
                        "naturalArea = ? WHERE id = ?",
                zone.getZoneNumber(), zone.getLetter(), zone.getMaximumCapacity(), zone.getCreationDate(),
                zone.getNaturalArea(), zone.getId());
    }

    /* Obté el zone amb el id donat. Torna null si no existeix. */
    public Zone getZone(String id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM Zone WHERE id = ?",
                    new ZoneRowMapper(), id);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    /* Obté tots els zones. Torna una llista buida si no n'hi ha cap. */
    public List<Zone> getZones() {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Zone",
                    new ZoneRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Zone>();
        }
    }
}