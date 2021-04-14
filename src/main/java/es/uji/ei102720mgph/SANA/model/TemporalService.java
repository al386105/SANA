package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class TemporalService {

    private String openningDays;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime beginningTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate beginningDate;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
    private String service;
    private String naturalArea;

    public TemporalService(){
        super();
    }

    public String getOpenningDays() {
        return openningDays;
    }

    public void setOpenningDays(String openningDays) {
        this.openningDays = openningDays;
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

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(String naturalArea) {
        this.naturalArea = naturalArea;
    }

    @Override
    public String toString(){
        return "TemporalService{" +
                "openningDays='" + openningDays + '\'' +
                ", beginningTime='" + beginningTime + '\'' +
                ", endTime=" + endTime +
                ", beginningDate='" + beginningDate+ '\'' +
                ", endDate='" + endDate + '\'' +
                ", service='" + service + '\'' +
                ", naturalArea='" + naturalArea + '\'' +
                '}';
    }
}
