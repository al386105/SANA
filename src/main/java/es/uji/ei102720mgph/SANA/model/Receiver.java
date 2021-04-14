package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;


/** This class must be extended by RegisteredCitizen, ControlStaff and MunicipalManager*/
public abstract class Receiver {
    private String email;
    private String name;
    private String surname;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate registrationDate;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate leavingDate;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
        return "Receiver{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", registrationDate=" + registrationDate +
                ", leavingDate=" + leavingDate +
                '}';
    }
}
