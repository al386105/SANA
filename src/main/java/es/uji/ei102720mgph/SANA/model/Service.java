package es.uji.ei102720mgph.SANA.model;

public class Service {
    private  String nameOfService;
    private int temporality;
    private String description;
    private String hiringPlace;

    public Service(){
    }

    public String getNameOfService() {
        return nameOfService;
    }

    public void setNameOfService(String nameOfService) {
        this.nameOfService = nameOfService;
    }

    public int getTemporality() {
        return temporality;
    }

    public void setTemporality(int temporality) {
        this.temporality = temporality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHiringPlace() {
        return hiringPlace;
    }

    public void setHiringPlace(String hiringPlace) {
        this.hiringPlace = hiringPlace;
    }

    @Override
    public String toString() {
        return "Service{" +
                "nameOfService='" + nameOfService + '\'' +
                ", temporality='" + temporality + '\'' +
                ", description=" + description +
                ", hiringPlace='" + hiringPlace + '\'' +
                '}';
    }

}
