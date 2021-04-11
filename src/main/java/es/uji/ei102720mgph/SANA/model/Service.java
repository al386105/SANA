package es.uji.ei102720mgph.SANA.model;

import es.uji.ei102720mgph.SANA.enums.Temporality;

public class Service {
    private  String nameOfService;
    private Temporality temporality;
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

    public Temporality getTemporality() {
        return temporality;
    }

    public void setTemporality(Temporality temporality) {
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
<<<<<<< Updated upstream
                ", temporality='" + temporality + '\'' +
                ", description=" + description +
                ", hiringPlace='" + hiringPlace + '\'' +
                '}';
    }

=======
                ", temporality=" + temporality +
                ", description='" + description + '\'' +
                ", hiringPlace='" + hiringPlace + '\'' +
                '}';
    }
>>>>>>> Stashed changes
}
