package es.uji.ei102720mgph.SANA.model;

import java.sql.Date;

public class ServiceDate {
    private String id;
    private Date beginningDate;
    private Date endDate;
    private String service;
    private String naturalArea;

    public ServiceDate(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(Date beginningDate) {
        this.beginningDate = beginningDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getNaturalArea() {
        return naturalArea;
    }

    public void setNaturalArea(String naturalArea) {
        this.naturalArea = naturalArea;
    }

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    @Override
    public String toString() {
        return "ServiceDate{" +
                "id='" + id + '\'' +
<<<<<<< Updated upstream
                ", beginningDate='" + beginningDate + '\'' +
                ", endDate=" + endDate	 +
=======
                ", beginningDate=" + beginningDate +
                ", endDate=" + endDate +
>>>>>>> Stashed changes
                ", service='" + service + '\'' +
                ", naturalArea='" + naturalArea + '\'' +
                '}';
    }
<<<<<<< Updated upstream



=======
>>>>>>> Stashed changes
}
