package es.uji.ei102720mgph.SANA.model;

import es.uji.ei102720mgph.SANA.enums.ReservationState;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaDatosMunicipal implements Comparable<ReservaDatosMunicipal>{
    private int reservationNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;
    private int numberOfPeople;
    private ReservationState state;
    private String QRcode;
    private int zoneNumber;
    private char letter;
    private String naturalArea;
    private LocalTime beginningTime;
    private LocalTime endTime;
    private String name;
    private String surname;

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

    public int getZoneNumber() {
        return zoneNumber;
    }

    public void setZoneNumber(int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "ReservaDatosMunicipal{" +
                "reservationNumber=" + reservationNumber +
                ", reservationDate=" + reservationDate +
                ", numberOfPeople=" + numberOfPeople +
                ", state=" + state +
                ", QRcode='" + QRcode + '\'' +
                ", zoneNumber=" + zoneNumber +
                ", letter=" + letter +
                ", naturalArea='" + naturalArea + '\'' +
                ", beginningTime=" + beginningTime +
                ", endTime=" + endTime +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }

    @Override
    public int compareTo(ReservaDatosMunicipal otra) {
        return -this.getReservationDate().compareTo(otra.getReservationDate());
    }
}
