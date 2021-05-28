package es.uji.ei102720mgph.SANA.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ServiceDateList extends Service {
    private String id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginningDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public ServiceDateList(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(LocalDate beginningDate) {
        this.beginningDate = beginningDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
