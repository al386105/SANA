package es.uji.ei102720mgph.SANA.model;

import es.uji.ei102720mgph.SANA.enums.Orientation;
import es.uji.ei102720mgph.SANA.enums.TypeOfAccess;
import es.uji.ei102720mgph.SANA.enums.TypeOfArea;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class NaturalArea {
    private String name;
    private TypeOfAccess typeOfAccess;
    private String geographicalLocation;
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

    public NaturalArea(){
        super();
    }

    public String getName() {
        return name;
    }

    public TypeOfAccess getTypeOfAccess() {
        return typeOfAccess;
    }

    public String getGeographicalLocation() {
        return geographicalLocation;
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

    public void setGeographicalLocation(String geographicalLocation) {
        this.geographicalLocation = geographicalLocation;
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

    @Override
    public String toString() {
        return "NaturalArea{" +
                "name='" + name + '\'' +
                ", typeOfAccess=" + typeOfAccess +
                ", geographicalLocation='" + geographicalLocation + '\'' +
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
