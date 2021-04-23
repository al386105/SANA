package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ServiceDate {
    private String id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginningDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String service;
    private String naturalArea;
    private static int contador = 1;

    public ServiceDate(){
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
        ServiceDate.contador = contador;
    }

    public static void incrementaContador() {
        ServiceDate.contador++;
    }

    @Override
    public String toString() {
        return "ServiceDate{" +
                "id='" + id + '\'' +
                ", beginningDate=" + beginningDate +
                ", endDate=" + endDate +
                ", service='" + service + '\'' +
                ", naturalArea='" + naturalArea + '\'' +
                '}';
    }
}
