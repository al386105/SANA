package es.uji.ei102720mgph.SANA.enums;

public enum TypeOfPeriod {
    daily("Por día"),
    monthly("Por mes"),
    annual("Por año");

    private String descripcion;

    private TypeOfPeriod(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
