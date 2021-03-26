package es.uji.ei102720mgph.SANA.model;

public class Picture {
    private String picturePath;
    private String naturalArea;

    public Picture(){

    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(String naturalArea) {
        this.naturalArea = naturalArea;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "picturePath='" + picturePath + '\'' +
                ", naturalArea='" + naturalArea + '\'' +
                '}';
    }
}
