package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Comment;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public final class CommentRowMapper implements RowMapper<Comment> {
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException{
        Comment comment = new Comment();
        comment.setCommentId(rs.getString("commentId"));
        comment.setCommentBody(rs.getString("commentBody"));
        comment.setScore(rs.getInt("score"));
        comment.setDate(rs.getObject("date", LocalDate.class));
        comment.setCitizenEmail(rs.getString("citizenEmail"));
        comment.setNaturaArea(rs.getString("naturalArea"));
        return comment;
    }
}
