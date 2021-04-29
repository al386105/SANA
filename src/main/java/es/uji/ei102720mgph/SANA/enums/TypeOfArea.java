package es.uji.ei102720mgph.SANA.enums;

public enum TypeOfArea {
    beach("Playa"),
    river("Río"),
    pond("Estanque"),
    lake("Lago"),
    forest("Bosque"),
    other("Otro");

    private String descripcion;

    private TypeOfArea(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
