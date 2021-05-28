package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot implements Comparable<TimeSlot> {
    private String id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginningDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime beginningTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private String naturalArea;
    private static int contador = 1;

    public TimeSlot(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(LocalDate beginningDate) {
        this.beginningDate = beginningDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getBeginningTime() {
        return beginningTime;
    }

    public void setBeginningTime(LocalTime beginningTime) {
        this.beginningTime = beginningTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(String naturalArea) {
        this.naturalArea = naturalArea;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        TimeSlot.contador = contador;
    }

    public static void incrementaContador() {
        TimeSlot.contador++;
    }

    @Override
    public int compareTo(TimeSlot altre) {
        return this.getId().compareTo(altre.getId());
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "id='" + id + '\'' +
                ", beginningDate=" + beginningDate +
                ", endDate=" + endDate +
                ", beginningTime=" + beginningTime +
                ", endTime=" + endTime +
                ", naturalArea='" + naturalArea + '\'' +
                '}';
    }
}
