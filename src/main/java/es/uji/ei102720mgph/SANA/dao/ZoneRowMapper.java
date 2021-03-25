package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Zone;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class ZoneRowMapper implements RowMapper<Zone> {

    public Zone mapRow(ResultSet rs, int rowNum) throws SQLException {
        Zone zone = new Zone();
        zone.setZoneNumber(rs.getInt("zoneNumber"));
        zone.setLetter((Character) rs.getObject("letter")); //???????????
        zone.setMaximumCapacity(rs.getInt("maximumCapacity"));
        zone.setCreationDate(rs.getObject("creationDate", LocalDate.class));
        zone.setNaturalArea(rs.getString("naturalArea"));
        return zone;
    }
}
