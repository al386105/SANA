package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.ControlStaff;
import es.uji.ei102720mgph.SANA.model.Email;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ControlStaffRowMapper implements RowMapper<ControlStaff> {
    public ControlStaff mapRow(ResultSet rs, int rowNum) throws SQLException {
        ControlStaff controlStaff = new ControlStaff();
        controlStaff.setEmail(rs.getString("email"));
        controlStaff.setName(rs.getString("username"));
        controlStaff.setPassword(rs.getString("password"));
        return controlStaff;
    }
}
