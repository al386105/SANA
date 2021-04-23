package es.uji.ei102720mgph.SANA.model;

public class ReservationOfZone {
    private String id;
    private int reservationNumber;
    private String zoneId;
    private static int contador = 1;

    public ReservationOfZone(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(int reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        ReservationOfZone.contador = contador;
    }

    public static void incrementaContador() {
        ReservationOfZone.contador++;
    }

    @Override
    public String toString() {
        return "ReservationOfZone{" +
                "id='" + id + '\'' +
                ", reservationNumber=" + reservationNumber +
                ", zoneId='" + zoneId + '\'' +
                '}';
    }
}
