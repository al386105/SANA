package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Formatter;
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
        boolean excepcion;
        Formatter fmt;
        do {
            try {
                fmt = new Formatter();
                jdbcTemplate.update(
                        "INSERT INTO Zone VALUES(?, ?, ?, ?, ?, ?)",
                        "z" + fmt.format("%08d", Zone.getContador()), zone.getZoneNumber(), zone.getLetter(),
                        zone.getMaximumCapacity(), LocalDate.now(), zone.getNaturalArea());
                excepcion = false;
            } catch (DuplicateKeyException e) {
                excepcion = true;
            } finally {
                Zone.incrementaContador();
            }
        } while (excepcion);
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

    public List<Zone> getZoneDisponibles(LocalDate fecha, String timeslot, int personas, String natArea) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM zone " +
                            "WHERE naturalArea = ? " +
                            "AND maximumcapacity >= ? " +
                            "AND (zonenumber, letter) NOT IN (SELECT z.zonenumber, z.letter FROM reservation AS r JOIN reservationofzone AS rz ON rz.reservationnumber = r.reservationnumber JOIN zone AS z ON z.id = rz.zoneid WHERE CAST(reservationdate AS date) = CAST(? AS date) AND timeslotid = ?);",
                    new ZoneRowMapper(), natArea, personas, fecha, timeslot);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Zone> getZonesOfNaturalArea(String naturalArea) {
        try {
            return jdbcTemplate.query("SELECT * FROM Zone WHERE naturalArea =?",
                    new ZoneRowMapper(), naturalArea);
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