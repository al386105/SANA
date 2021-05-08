package es.uji.ei102720mgph.SANA.enums;

public enum DaysOfWeek {
    L("Lunes"),
    M("Martes"),
    X("Miércoles"),
    J("Jueves"),
    V("Viernes"),
    S("Sábado"),
    D("Doming");

    private String descripcion;

    private DaysOfWeek(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
