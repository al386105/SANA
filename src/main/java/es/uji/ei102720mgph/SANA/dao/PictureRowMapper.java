package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Picture;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class PictureRowMapper implements RowMapper<Picture> {
    public Picture mapRow(ResultSet rs, int rowNum) throws SQLException{
        Picture picture = new Picture();
        picture.setPicturePath(rs.getString("picturePath"));
        picture.setNaturalArea(rs.getString("naturalArea"));
        return picture;
    }
}
