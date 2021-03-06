package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Municipality implements Comparable<Municipality> {
    private String name;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

    public Municipality(){
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public int compareTo(Municipality altre) {
        return this.getName().compareTo(altre.getName());
    }

    @Override
    public String toString() {
        return "Municipality{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
