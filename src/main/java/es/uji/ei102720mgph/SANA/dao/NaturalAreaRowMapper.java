package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.Orientation;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.enums.TypeOfArea;
import es.uji.ei102720mgph.SANA.model.NaturalArea;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public final class NaturalAreaRowMapper implements RowMapper<NaturalArea> {
    public NaturalArea mapRow(ResultSet rs, int rowNum) throws SQLException{
        NaturalArea naturalArea = new NaturalArea();
        naturalArea.setName(rs.getString("name"));
        naturalArea.setTypeOfAccess(rs.getObject("typeOfAccess", TypeOfAccess.class));
        naturalArea.setGeographicalLocation(rs.getString("geographicalLocation"));
        naturalArea.setTypeOfArea(rs.getObject("typeOfArea", TypeOfArea.class));
        naturalArea.setLength(rs.getFloat("length"));
        naturalArea.setWidth(rs.getFloat("width"));
        naturalArea.setPhysicalCharacteristics(rs.getString("physicalCharacteristics"));
        naturalArea.setDescription(rs.getString("description"));
        naturalArea.setOrientation(rs.getObject("orientation", Orientation.class));
        naturalArea.setRestrictionTimePeriod(rs.getObject("restrictionTimePeriod", LocalDate.class));
        naturalArea.setOccupancyRate(rs.getFloat("occupancyRate"));
        naturalArea.setMunicipality(rs.getString("municipality"));
        return naturalArea;
    }
}

