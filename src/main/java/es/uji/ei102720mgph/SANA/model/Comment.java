package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Comment {
    private String commentId;
    private String commentBody;
    private Integer score;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String citizenEmail;
    private String naturalArea;
    private static int contador = 1;

    public Comment(){
        super();
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

    public String getCitizenEmail() {
        return citizenEmail;
    }

    public void setCitizenEmail(String citizenId) {
        this.citizenEmail = citizenId;
    }

    public String getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(String naturaArea) {
        this.naturalArea = naturaArea;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Comment.contador = contador;
    }

    public static void incrementaContador() {
        Comment.contador++;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", commentBody='" + commentBody + '\'' +
                ", score=" + score +
                ", date=" + date +
                ", citizenEmail='" + citizenEmail + '\'' +
                ", naturaArea='" + naturalArea + '\'' +
                '}';
    }
}
