package es.uji.ei102720mgph.SANA.enums;

public enum ReservationState {
    created("Creada"),
    used("Usada"),
    inUse("En uso"),
    cancelledMunicipalManager("Cancelada por gestor municipal"),
    cancelledCitizen("Cancelada por ciudadano"),
    cancelledControlStaff("Cancelada por personal de control");

    private String descripcion;

    private ReservationState(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
