package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot {
    private String id;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate beginningDate;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime beginningTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private String naturalArea;

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
