package es.uji.ei102720mgph.SANA.model;

import es.uji.ei102720mgph.SANA.enums.TypeOfOccupancy;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

/**Esta clase se ha implementado para gestionar de forma mas sencilla el forumulario de ocupación
 * Para no tener que pasar como PathParam naturalArea, fecha, hora, etc
 * */
public class OccupancyFormData {
    private String naturalArea;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
//    @DateTimeFormat(pattern = "HH:mm")
//    private LocalTime creationTime;
    private TypeOfOccupancy typeOfOccupancy;

    public OccupancyFormData(){

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

//    public LocalTime getCreationTime() {
//        return creationTime;
//    }
//
//    public void setCreationTime(LocalTime creationTime) {
//        this.creationTime = creationTime;
//    }
//
    public TypeOfOccupancy getTypeOfOccupancy() {
        return typeOfOccupancy;
    }

    public void setTypeOfOccupancy(TypeOfOccupancy typeOfOccupancy) {
        this.typeOfOccupancy = typeOfOccupancy;
    }
//
//    @Override
//    public String toString() {
//        return "OccupancyFormData{" +
//                "naturalArea=" + naturalArea +
//                ", date=" + date +
//                ", creationTime=" + creationTime +
//                ", typeOfOccupancy=" + typeOfOccupancy +
//                '}';
//    }
}
