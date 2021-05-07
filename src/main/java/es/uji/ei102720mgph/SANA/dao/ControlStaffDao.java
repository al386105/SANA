package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.ControlStaff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;

@Repository
public class ControlStaffDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public ControlStaff getControlStaf(String email){
        try{

            return jdbcTemplate.queryForObject("SELECT * FROM ControlStaff WHERE email=?",
                    new ControlStaffRowMapper(), email);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
