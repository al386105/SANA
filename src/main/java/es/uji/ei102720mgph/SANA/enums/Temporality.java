package es.uji.ei102720mgph.SANA.enums;

public enum Temporality {
    fixed("Fijo"),
    temporal("Temporal");

    private String descripcion;

    private Temporality(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
