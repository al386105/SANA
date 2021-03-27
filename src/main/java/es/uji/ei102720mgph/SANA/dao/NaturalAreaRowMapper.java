package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.enums.Orientation;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.enums.TypeOfArea;
import es.uji.ei102720mgph.SANA.model.NaturalArea;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


public final class NaturalAreaRowMapper implements RowMapper<NaturalArea> {
    public NaturalArea mapRow(ResultSet rs, int rowNum) throws SQLException{
        NaturalArea naturalArea = new NaturalArea();
        naturalArea.setName(rs.getString("name"));
        TypeOfAccess access = TypeOfAccess.valueOf(rs.getString("typeOfAccess"));
        naturalArea.setTypeOfAccess(access);
        naturalArea.setGeographicalLocation(rs.getString("geographicalLocation"));
        TypeOfArea area = TypeOfArea.valueOf(rs.getString("typeOfArea"));
        naturalArea.setTypeOfArea(area);
        naturalArea.setLength(rs.getFloat("length"));
        naturalArea.setWidth(rs.getFloat("width"));
        naturalArea.setPhysicalCharacteristics(rs.getString("physicalCharacteristics"));
        naturalArea.setDescription(rs.getString("description"));
        Orientation orientation = Orientation.valueOf(rs.getString("orientation"));
        naturalArea.setOrientation(orientation);
        Date d = rs.getDate("restrictionTimePeriod");
        naturalArea.setRestrictionTimePeriod(d != null ? d.toLocalDate() : null);
        Float f = rs.getFloat("occupancyRate");
        naturalArea.setOccupancyRate(f != null ? f : -1);
        naturalArea.setMunicipality(rs.getString("municipality"));
        return naturalArea;
    }
}

