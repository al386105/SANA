package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PictureDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addPicture(Picture picture) {
        jdbcTemplate.update("INSERT INTO Picture VALUES(?, ?)",
                picture.getPicturePath(), picture.getNaturalArea());
    }

    public void deletePicture(String picturePath){
        jdbcTemplate.update("DELETE FROM Picture WHERE picturePath = ?",
                picturePath);
    }

    public Picture getPicture(String picturePath) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Picture WHERE picturePath = ?",
                    new PictureRowMapper(),
                    picturePath);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Picture> getPicturesOfNaturalArea(String naturalArea) {
        try {
            return jdbcTemplate.query("SELECT * FROM Picture WHERE naturalArea = ?",
                    new PictureRowMapper(),
                    naturalArea);
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Picture>();
        }
    }
}
