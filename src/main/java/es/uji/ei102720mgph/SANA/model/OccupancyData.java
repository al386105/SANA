package es.uji.ei102720mgph.SANA.model;

import es.uji.ei102720mgph.SANA.model.NaturalArea;

public class OccupancyData {
    private NaturalArea naturalArea;
    private float occupancyRate;
    private int maxCapacity;
    private int totalOccupancy;

    public OccupancyData(){

    }

    public NaturalArea getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(NaturalArea naturalArea) {
        this.naturalArea = naturalArea;
    }

    public float getOccupancyRate() {
        return occupancyRate;
    }

    public void setOccupancyRate(float occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getTotalOccupancy() {
        return totalOccupancy;
    }

    public void setTotalOccupancy(int totalOccupancy) {
        this.totalOccupancy = totalOccupancy;
    }

    @Override
    public String toString() {
        return "OccupancyData{" +
                "naturalArea='" + naturalArea + '\'' +
                ", occupancyRate=" + occupancyRate +
                ", maxCapacity=" + maxCapacity +
                ", totalOccupancy=" + totalOccupancy +
                '}';
    }
}
