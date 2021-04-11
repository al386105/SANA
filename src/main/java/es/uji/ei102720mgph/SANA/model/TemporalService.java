package es.uji.ei102720mgph.SANA.model;

import java.sql.Date;
import java.sql.Time;

public class TemporalService {

    private int openningDays;
    private Time beginningTime;
    private Time endTime;
    private Date beginningDate;
    private Date endDate;
    private String service;
    private String naturalArea;

    public TemporalService(){
    }

    public int getOpenningDays() {
        return openningDays;
    }

    public void setOpenningDays(int openningDays) {
        this.openningDays = openningDays;
    }

    public Time getBeginningTime() {
        return beginningTime;
    }

    public void setBeginningTime(Time beginningTime) {
        this.beginningTime = beginningTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
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

    @Override
    public String toString(){
        return "TemporalService{" +
                "openningDays='" + openningDays + '\'' +
                ", beginningTime='" + beginningTime + '\'' +
                ", endTime=" + endTime +
                ", beginningDate='" + beginningDate+ '\'' +
                ", endDate='" + endDate + '\'' +
                ", service='" + service + '\'' +
                ", naturalArea='" + naturalArea + '\'' +
                '}';
    }
}
