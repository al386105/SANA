package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class TemporalService implements Comparable<TemporalService>  {
    private String id;
    private String openingDays;
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
    private static int contador = 1;

    public TemporalService(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpeningDays() {
        return openingDays;
    }

    public void setOpeningDays(String openingDays) {
        this.openingDays = openingDays;
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

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        TemporalService.contador = contador;
    }

    public static void incrementaContador() {
        TemporalService.contador++;
    }

    public int compareTo(TemporalService altre) {
        return this.getService().compareTo(altre.getService());
    }

    @Override
    public String toString() {
        return "TemporalService{" +
                "id='" + id + '\'' +
                ", openingDays='" + openingDays + '\'' +
                ", beginningTime=" + beginningTime +
                ", endTime=" + endTime +
                ", beginningDate=" + beginningDate +
                ", endDate=" + endDate +
                ", service='" + service + '\'' +
                ", naturalArea='" + naturalArea + '\'' +
                '}';
    }
}
