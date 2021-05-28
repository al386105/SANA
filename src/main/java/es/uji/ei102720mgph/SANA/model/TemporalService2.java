package es.uji.ei102720mgph.SANA.model;

import es.uji.ei102720mgph.SANA.enums.DaysOfWeek;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TemporalService2 implements Comparable<TemporalService2>  {
    private String id;
    private List<DaysOfWeek> diasMarcados;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime beginningTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginningDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String service;
    private String naturalArea;

    public TemporalService2(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DaysOfWeek> getDiasMarcados() {
        return diasMarcados;
    }

    public void setDiasMarcados(List<DaysOfWeek> diasMarcados) {
        this.diasMarcados = diasMarcados;
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
    public int compareTo(TemporalService2 altre) {
        return this.getService().compareTo(altre.getService());
    }

    @Override
    public String toString() {
        return "TemporalService2{" +
                "id='" + id + '\'' +
                ", diasMarcados=" + diasMarcados +
                ", beginningTime=" + beginningTime +
                ", endTime=" + endTime +
                ", beginningDate=" + beginningDate +
                ", endDate=" + endDate +
                ", service='" + service + '\'' +
                ", naturalArea='" + naturalArea + '\'' +
                '}';
    }
}
