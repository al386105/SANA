package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Assigned {
    private String controlStaffEmail;
    private String naturalArea;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    public Assigned(){
        super();
    }

    public String getControlStaffEmail() {
        return controlStaffEmail;
    }

    public void setControlStaffEmail(String controlStaffEmail) {
        this.controlStaffEmail = controlStaffEmail;
    }

    public String getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(String naturalArea) {
        this.naturalArea = naturalArea;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Assigned{" +
                "controlStaffEmail='" + controlStaffEmail + '\'' +
                ", naturalArea='" + naturalArea + '\'' +
                ", date=" + date +
                '}';
    }
}
