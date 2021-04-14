package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Receiver;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

//public final class ReceiverRowMapper implements RowMapper<Receiver> {
//    public Receiver mapRow(ResultSet rs, int rowNum) throws SQLException {
//        Receiver receiver = new Receiver();
//        receiver.setEmail(rs.getString("email"));
//        receiver.setName(rs.getString("name"));
//        receiver.setSurname(rs.getString("surname"));
//        receiver.setDateOfBirth(rs.getObject("dateOfBirth", LocalDate.class));
//        receiver.setRegistrationDate(rs.getObject("registrationDate", LocalDate.class));
//        Date d = rs.getDate("leavingDate");
//        receiver.setLeavingDate(d != null ? d.toLocalDate() : null);
//        return receiver;
//    }
//}