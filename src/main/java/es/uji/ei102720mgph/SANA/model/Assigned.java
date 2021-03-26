package es.uji.ei102720mgph.SANA.model;

public class Assigned {
    private String controlStaffEmail;
    private String naturalArea;

    public Assigned(){
    }

    public String getControlStaffEmail() {
        return controlStaffEmail;
    }

    public void setControlStaffEmail(String controlStaffEmail) {
        this.controlStaffEmail = controlStaffEmail;
    }

    public String getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(String naturalArea) {
        this.naturalArea = naturalArea;
    }

    @Override
    public String toString() {
        return "Assigned{" +
                "controlStaffEmail='" + controlStaffEmail + '\'' +
                ", naturalArea='" + naturalArea + '\'' +
                '}';
    }
}
