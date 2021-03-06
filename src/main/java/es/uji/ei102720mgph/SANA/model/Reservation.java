package es.uji.ei102720mgph.SANA.model;

import java.time.LocalDate;
import java.time.LocalTime;
import es.uji.ei102720mgph.SANA.enums.ReservationState;
import org.springframework.format.annotation.DateTimeFormat;

public class Reservation implements Comparable<Reservation> {
    private int reservationNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime creationTime;
    private int numberOfPeople;
    private ReservationState state;
    private String QRcode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate cancellationDate;
    private String cancellationReason;
    private String citizenEmail;
    private String timeSlotId;
    private static int contador = 1;

    public Reservation(){
        super();
    }

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

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalTime creationTime) {
        this.creationTime = creationTime;
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

    public LocalDate getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(LocalDate cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCitizenEmail() {
        return citizenEmail;
    }

    public void setCitizenEmail(String citizenEmail) {
        this.citizenEmail = citizenEmail;
    }

    public String getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        Reservation.contador = contador;
    }

    public static void incrementaContador() {
        Reservation.contador++;
    }

    @Override
    public int compareTo(Reservation altre) {
            return -this.getReservationDate().compareTo(altre.getReservationDate());
        }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationNumber=" + reservationNumber +
                ", reservationDate=" + reservationDate +
                ", creationDate=" + creationDate +
                ", creationTime=" + creationTime +
                ", numberOfPeople=" + numberOfPeople +
                ", state=" + state +
                ", QRcode='" + QRcode + '\'' +
                ", cancellationDate=" + cancellationDate +
                ", cancellationReason='" + cancellationReason + '\'' +
                ", citizenEmail='" + citizenEmail + '\'' +
                ", timeSlotId='" + timeSlotId + '\'' +
                '}';
    }
}
