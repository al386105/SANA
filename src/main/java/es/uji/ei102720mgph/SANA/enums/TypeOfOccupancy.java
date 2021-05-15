package es.uji.ei102720mgph.SANA.enums;

public enum TypeOfOccupancy {
    hourly("Por horas"),
    dayly("Diaria"),
    monthly("Mensual"),
    annual("Anual"),
    total("Total");

    private String descripcion;

    private TypeOfOccupancy(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
