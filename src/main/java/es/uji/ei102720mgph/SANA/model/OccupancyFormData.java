package es.uji.ei102720mgph.SANA.model;

import es.uji.ei102720mgph.SANA.enums.TypeOfPeriod;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class OccupancyFormData {
    private TypeOfPeriod typeOfPeriod;
    private String naturalArea;
    private int year;
    private int month;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate day;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hour;


    public OccupancyFormData(){

    }

    public TypeOfPeriod getTypeOfPeriod() {
        return typeOfPeriod;
    }

    public void setTypeOfPeriod(TypeOfPeriod typeOfPeriod) {
        this.typeOfPeriod = typeOfPeriod;
    }

    public String getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(String naturalArea) {
        this.naturalArea = naturalArea;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalTime getHour() {
        return hour;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "OccupancyFormData{" +
                "typeOfPeriod=" + typeOfPeriod +
                ", naturalArea='" + naturalArea + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                '}';
    }
}
