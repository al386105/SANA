package es.uji.ei102720mgph.SANA.dao;

import es.uji.ei102720mgph.SANA.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addComment(Comment comment){
        jdbcTemplate.update("INSERT INTO Comment VALUES(?, ?, ?, ?, ?, ?)",
                comment.getCommentId(), comment.getCommentBody(), comment.getScore(),
                comment.getDate(), comment.getCitizenId(), comment.getNaturaArea());
    }

    public void deleteComment(Comment comment){
        jdbcTemplate.update("DELETE FROM Comment WHERE commentId = ?",
                comment.getCommentId());
    }

    public void updateComment(Comment comment){
        jdbcTemplate.update("UPDATE Comment" +
                "SET commentBody = ?, score = ?, date = ?, " +
                "citizenId = ?, naturalArea = ?" +
                "WHERE commentId = ?",
                comment.getCommentBody(), comment.getScore(), comment.getDate(),
                comment.getCitizenId(), comment.getNaturaArea(),
                comment.getCitizenId());
    }

    public Comment getComment(String commentId) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Comment WHERE commentId = ?",
                    new CommentRowMapper(),
                    commentId);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Comment> getProves() {
        try {
            return jdbcTemplate.query("SELECT * FROM Comment",
                    new CommentRowMapper());
        }
        catch(EmptyResultDataAccessException e) {
            return new ArrayList<Comment>();
        }
    }
}
