package es.uji.ei102720mgph.SANA.enums;

public enum Months {
    Enero("Enero", 1),
    Febrero("Febrero", 2),
    Marzo("Marzo", 3),
    Abril("Abril", 4),
    Mayo("Mayo", 5),
    Junio("Junio", 6),
    Julio("Julio", 7),
    Agosto("Agosto", 8),
    Septiembre("Septiembre", 9),
    Octubre("Octubre", 10),
    Noviembre("Noviembre", 11),
    Diciembre("Diciembre", 12);

    private String descripcion;
    private int num;

    private Months(String descripcion, int num) {
        this.descripcion = descripcion;
        this.num = num;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getNum() { return num; }
}
