package es.uji.ei102720mgph.SANA.enums;

public enum Orientation {
    N("Norte"),
    S("Sur"),
    E("Este"),
    W("Oeste");

    private String descripcion;

    private Orientation(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
