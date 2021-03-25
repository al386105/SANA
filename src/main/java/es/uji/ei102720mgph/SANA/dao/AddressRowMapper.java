package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Address;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class AddressRowMapper implements RowMapper<Address> {

    public Address mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address address = new Address();
        address.setId(rs.getString("id"));
        address.setStreet(rs.getString("street"));
        address.setNumber(rs.getInt("number"));
        address.setFloorDoor(rs.getString("floorDoor"));
        address.setPostalCode(rs.getString("postalCode"));
        address.setCity(rs.getString("city"));
        address.setCountry(rs.getString("country"));
        return address;
    }
}
