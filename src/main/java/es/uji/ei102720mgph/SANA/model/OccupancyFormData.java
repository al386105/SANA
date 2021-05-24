package es.uji.ei102720mgph.SANA.model;

public class OccupancyFormData {
    private String naturalArea;
    private int year;

    public OccupancyFormData(){

    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(String naturalArea) {
        this.naturalArea = naturalArea;
    }

    @Override
    public String toString() {
        return "OccupancyFormData{" +
                "naturalArea='" + naturalArea + '\'' +
                ", year=" + year +
                '}';
    }
}
