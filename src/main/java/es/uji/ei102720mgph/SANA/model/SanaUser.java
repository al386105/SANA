package es.uji.ei102720mgph.SANA.model;

import es.uji.ei102720mgph.SANA.enums.TypeOfUser;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/** This class must be extended by RegisteredCitizen, ControlStaff and MunicipalManager*/
public abstract class SanaUser {
    private String email;
    private String name;
    private String surname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate leavingDate;
    private TypeOfUser typeOfUser;

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

    public TypeOfUser getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(TypeOfUser typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    @Override
    public String toString() {
        return "SanaUser{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", registrationDate=" + registrationDate +
                ", leavingDate=" + leavingDate +
                ", typeOfUser=" + typeOfUser +
                '}';
    }
}
