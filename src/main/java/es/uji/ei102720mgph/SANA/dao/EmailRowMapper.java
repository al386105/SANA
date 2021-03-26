package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Email;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class EmailRowMapper implements RowMapper<Email>{

    public Email mapRow(ResultSet rs, int rowNum) throws SQLException {
        Email email = new Email();
        email.setId(rs.getString("id"));
        email.setSubject(rs.getString("subject"));
        email.setTextBody(rs.getString("textBody"));
        email.setSender(rs.getString("sender"));
        email.setDate(rs.getObject("date", LocalDate.class));
        email.setReceiver(rs.getString("receiver"));
        return email;
    }
}
