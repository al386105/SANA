package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class Email {
    private String id;
    private String subject;
    private String textBody;
    private String sender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String sanaUser;
    private static int contador = 1;

    public Email(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTextBody() {
        return textBody;
    }

    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSanaUser() {
        return sanaUser;
    }

    public void setSanaUser(String sanaUser) {
        this.sanaUser = sanaUser;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Email.contador = contador;
    }

    public static void incrementaContador() {
        Email.contador++;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", textBody='" + textBody + '\'' +
                ", sender='" + sender + '\'' +
                ", date=" + date +
                ", sanaUSer='" + sanaUser + '\'' +
                '}';
    }
}
