package es.uji.ei102720mgph.SANA.model;

import es.uji.ei102720mgph.SANA.enums.Orientation;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.enums.TypeOfArea;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class NaturalAreaForm implements Comparable<NaturalAreaForm> {
    private String name;
    private TypeOfAccess typeOfAccess;
    private float latitudGrados;
    private float latitudMin;
    private float latitudSeg;
    private String latitudLetra;
    private float longitudGrados;
    private float longitudMin;
    private float longitudSeg;
    private String longitudLetra;
    private TypeOfArea typeOfArea;
    private float length;
    private float width;
    private String physicalCharacteristics;
    private String description;
    private Orientation orientation;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate restrictionTimePeriod;
    private float occupancyRate;
    private String municipality;

    public NaturalAreaForm(){
        super();
    }

    public String getName() {
        return name;
    }

    public TypeOfAccess getTypeOfAccess() {
        return typeOfAccess;
    }

    public float getLatitudGrados() {
        return latitudGrados;
    }

    public void setLatitudGrados(float latitudGrados) {
        this.latitudGrados = latitudGrados;
    }

    public float getLatitudMin() {
        return latitudMin;
    }

    public void setLatitudMin(float latitudMin) {
        this.latitudMin = latitudMin;
    }

    public float getLatitudSeg() {
        return latitudSeg;
    }

    public void setLatitudSeg(float latitudSeg) {
        this.latitudSeg = latitudSeg;
    }

    public String getLatitudLetra() {
        return latitudLetra;
    }

    public void setLatitudLetra(String latitudLetra) {
        this.latitudLetra = latitudLetra;
    }

    public float getLongitudGrados() {
        return longitudGrados;
    }

    public void setLongitudGrados(float longitudGrados) {
        this.longitudGrados = longitudGrados;
    }

    public float getLongitudMin() {
        return longitudMin;
    }

    public void setLongitudMin(float longitudMin) {
        this.longitudMin = longitudMin;
    }

    public float getLongitudSeg() {
        return longitudSeg;
    }

    public void setLongitudSeg(float longitudSeg) {
        this.longitudSeg = longitudSeg;
    }

    public String getLongitudLetra() {
        return longitudLetra;
    }

    public void setLongitudLetra(String longitudLetra) {
        this.longitudLetra = longitudLetra;
    }

    public TypeOfArea getTypeOfArea() {
        return typeOfArea;
    }

    public float getLength() {
        return length;
    }

    public float getWidth() {
        return width;
    }

    public String getPhysicalCharacteristics() {
        return physicalCharacteristics;
    }

    public String getDescription() {
        return description;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public LocalDate getRestrictionTimePeriod() {
        return restrictionTimePeriod;
    }

    public float getOccupancyRate() {
        return occupancyRate;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeOfAccess(TypeOfAccess typeOfAccess) {
        this.typeOfAccess = typeOfAccess;
    }

    public void setTypeOfArea(TypeOfArea typeOfArea) {
        this.typeOfArea = typeOfArea;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setPhysicalCharacteristics(String physicalCharacteristics) {
        this.physicalCharacteristics = physicalCharacteristics;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void setRestrictionTimePeriod(LocalDate restrictionTimePeriod) {
        this.restrictionTimePeriod = restrictionTimePeriod;
    }

    public void setOccupancyRate(float occupancyRate) {
        this.occupancyRate = occupancyRate;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public int compareTo(NaturalAreaForm altre) {
        return this.getName().compareTo(altre.getName());
    }

    @Override
    public String toString() {
        return "NaturalAreaForm{" +
                "name='" + name + '\'' +
                ", typeOfAccess=" + typeOfAccess +
                ", latitudGrados=" + latitudGrados +
                ", latitudMin=" + latitudMin +
                ", latitudSeg=" + latitudSeg +
                ", latitudLetra='" + latitudLetra + '\'' +
                ", longitudGrados=" + longitudGrados +
                ", longitudMin=" + longitudMin +
                ", longitudSeg=" + longitudSeg +
                ", longitudLetra='" + longitudLetra + '\'' +
                ", typeOfArea=" + typeOfArea +
                ", length=" + length +
                ", width=" + width +
                ", physicalCharacteristics='" + physicalCharacteristics + '\'' +
                ", description='" + description + '\'' +
                ", orientation=" + orientation +
                ", restrictionTimePeriod=" + restrictionTimePeriod +
                ", occupancyRate=" + occupancyRate +
                ", municipality='" + municipality + '\'' +
                '}';
    }
}
