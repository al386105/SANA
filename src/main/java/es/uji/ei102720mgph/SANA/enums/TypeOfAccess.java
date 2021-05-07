package es.uji.ei102720mgph.SANA.enums;

public enum TypeOfAccess {
    restricted("Restringido"),
    nonRestricted("No restringido"),
    closed("Cerrado");

    private String descripcion;

    private TypeOfAccess(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
