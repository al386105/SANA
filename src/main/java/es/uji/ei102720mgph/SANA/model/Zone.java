package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class Zone {
    private int zoneNumber;
    private char letter;
    private int maximumCapacity;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;
    private String naturalArea;

    public Zone(){
    }

    public int getZoneNumber() {
        return zoneNumber;
    }

    public void setZoneNumber(int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(String naturalArea) {
        this.naturalArea = naturalArea;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "zoneNumber=" + zoneNumber +
                ", letter=" + letter +
                ", maximumCapacity=" + maximumCapacity +
                ", creationDate=" + creationDate +
                ", naturalArea='" + naturalArea + '\'' +
                '}';
    }
}
