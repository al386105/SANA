package es.uji.ei102720mgph.SANA.services;

import es.uji.ei102720mgph.SANA.dao.ReservationDao;
import es.uji.ei102720mgph.SANA.dao.ZoneDao;
import es.uji.ei102720mgph.SANA.model.Reservation;
import es.uji.ei102720mgph.SANA.model.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OccupationSvc implements OccupationService{

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    ZoneDao zoneDao;

    public int getTotalOccupancy(String naturalArea){
        List<Reservation> reservations = reservationDao.getReservationsOfNaturalArea(naturalArea);
        int occupancy = 0;
        for (Reservation reservation: reservations){
            if (reservation.getState().getDescripcion().equals("Usada")) occupancy += reservation.getNumberOfPeople();
        }
        return occupancy;
    }


    public int getOccupancyOfDay(String naturalArea, LocalDate date){
        List<Reservation> reservations = reservationDao.getReservationsOfNaturalAreaOfDay(naturalArea, date);
        int occupancy = 0;
        for(Reservation reservation: reservations){
            if(reservation.getState().getDescripcion().equals("Usada")) occupancy += reservation.getNumberOfPeople();
        }
        return occupancy;
    }

    public int getMaxCapacityOfNaturalArea(String naturalArea){
        List<Zone> zones = zoneDao.getZonesOfNaturalArea(naturalArea);
        int maxCapacity = 0;
        for (Zone zone: zones){
            maxCapacity += zone.getMaximumCapacity();
        }
        return maxCapacity;
    }

    public float getRateDayOccupancyOfNaturalArea(String naturalArea, LocalDate date){
        int maxCapacity = getMaxCapacityOfNaturalArea(naturalArea);
        int occupancyOfDay = getOccupancyOfDay(naturalArea, date);
        return ((float) occupancyOfDay / (float) maxCapacity) * 100;
    }
}
