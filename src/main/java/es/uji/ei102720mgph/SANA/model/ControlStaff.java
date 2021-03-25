package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class ControlStaff {
    private String name;
    private String surname;
    private String user;
    private String password; //hacer encrypted
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;
    private LocalDate leavingDate;

    public ControlStaff() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(LocalDate leavingDate) {
        this.leavingDate = leavingDate;
    }

    @Override
    public String toString() {
        return "ControlStaff{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", leavingDate=" + leavingDate +
                '}';
    }
}
