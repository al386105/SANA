package es.uji.ei102720mgph.SANA.model;


import java.time.LocalDate;

public class Comment {
    private String commentId;
    private String commentBody;
    private Integer score;
    private LocalDate date;
    private String citizenId;
    private String naturaArea;

    public Comment(){

    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getNaturaArea() {
        return naturaArea;
    }

    public void setNaturaArea(String naturaArea) {
        this.naturaArea = naturaArea;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", commentBody='" + commentBody + '\'' +
                ", score=" + score +
                ", date=" + date +
                ", citizenId='" + citizenId + '\'' +
                ", naturaArea='" + naturaArea + '\'' +
                '}';
    }
}
