package es.uji.ei102720mgph.SANA.model;

import es.uji.ei102720mgph.SANA.enums.ReservationState;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservaDatosAgrupada implements Comparable<ReservaDatos> {
    private int reservationNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;
    private int numberOfPeople;
    private ReservationState state;
    private String QRcode;
    private List<String> zonas;
    private String naturalArea;
    private LocalTime beginningTime;
    private LocalTime endTime;

    public int getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(int reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public ReservationState getState() {
        return state;
    }

    public void setState(ReservationState state) {
        this.state = state;
    }

    public String getQRcode() {
        return QRcode;
    }

    public void setQRcode(String QRcode) {
        this.QRcode = QRcode;
    }

    public List<String> getZonas() {
        return zonas;
    }

    public void setZonas(List<String> zonas) {
        this.zonas = zonas;
    }

    public String getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(String naturalArea) {
        this.naturalArea = naturalArea;
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

    @Override
    public String toString() {
        return "ReservaDatosAgrupada{" +
                "reservationNumber=" + reservationNumber +
                ", reservationDate=" + reservationDate +
                ", numberOfPeople=" + numberOfPeople +
                ", state=" + state +
                ", QRcode='" + QRcode + '\'' +
                ", zonas=" + zonas +
                ", naturalArea='" + naturalArea + '\'' +
                ", beginningTime=" + beginningTime +
                ", endTime=" + endTime +
                '}';
    }

    @Override
    public int compareTo(ReservaDatos otra) {
        return -this.getReservationDate().compareTo(otra.getReservationDate());
    }

}
