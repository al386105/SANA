package es.uji.ei102720mgph.SANA.services;

import es.uji.ei102720mgph.SANA.model.OccupancyData;
import es.uji.ei102720mgph.SANA.dao.ReservationDao;
import es.uji.ei102720mgph.SANA.dao.ZoneDao;
import es.uji.ei102720mgph.SANA.model.NaturalArea;
import es.uji.ei102720mgph.SANA.model.Reservation;
import es.uji.ei102720mgph.SANA.model.Zone;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OccupationSvc implements OccupationService{

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    ZoneDao zoneDao;

    private int getOccupancy(List<Reservation> reservations){
        int occupancy = 0;
        for(Reservation reservation: reservations){
            if(reservation.getState().getDescripcion().equals("Usada")) occupancy += reservation.getNumberOfPeople();
        }
        return occupancy;
    }

    private int getMaxCapacityOfNaturalArea(String naturalArea){
        List<Zone> zones = zoneDao.getZonesOfNaturalArea(naturalArea);
        int maxCapacity = 0;
        for (Zone zone: zones){
            maxCapacity += zone.getMaximumCapacity();
        }
        return maxCapacity;
    }

    private float getRateOccupancyByHour(String naturalArea, LocalDate date, LocalTime time){
        int maxCapacity = getMaxCapacityOfNaturalArea(naturalArea);
        int occupancyOfHour = getOccupancy(
                reservationDao.getReservationsOfNaturalAreaOfHour(naturalArea, date, time));
        return ((float) occupancyOfHour / (float) maxCapacity) * 100;
    }

    public int getTotalOccupancy(String naturalArea){
        List<Reservation> reservations = reservationDao.getReservationsOfNaturalArea(naturalArea);
        return getOccupancy(reservations);
    }

    public List<OccupancyData> getOccupancyDataOfNaturalAreas(List<NaturalArea> naturalAreas){
        List<OccupancyData> occupancyDataOfNaturalAreas = new ArrayList<>(naturalAreas.size());
        for(NaturalArea naturalArea: naturalAreas){
            String naturalAreaName = naturalArea.getName();
            OccupancyData occupancyData = new OccupancyData();
            occupancyData.setNaturalArea(naturalArea);
            occupancyData.setMaxCapacity(getMaxCapacityOfNaturalArea(naturalAreaName));
            occupancyData.setTotalOccupancy(getTotalOccupancy(naturalArea.getName()));
            occupancyData.setOccupancyRate(getRateOccupancyByHour(naturalAreaName, LocalDate.now(), LocalTime.now()));
            occupancyDataOfNaturalAreas.add(occupancyData);
        }
        return occupancyDataOfNaturalAreas;
    }


    public String getOccupancyPlotByYear(String naturalArea, int year) {
        String pathPicture = "/assets/img/plots/" +  naturalArea + "_" + year + ".png";
        String path = "src/main/resources/static/assets/img/plots/" + naturalArea + "_" + year + ".png";
        File file = new File(path);

        //comprobamos que el gráfico no se haya generado previamente????
        //if (file.exists()){
        //    return pathPicture;
        //}

        //Generamos el dataSet:
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate date;
        int occupancy;
        int months = 12;
        for(int month = 1; month <= months; month++){
            date = LocalDate.of(year, month, 1);
            occupancy = getOccupancy(reservationDao.getReservationsOfNaturalAreaOfMonth(naturalArea, date));
            dataset.addValue(occupancy, naturalArea, month  + "");
        }

        //Generamos el chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Ocupacion en  " + naturalArea + " durante " + year,
                "Mes",
                "Ocupación",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        //Guardamos la imagen
        try {
            ChartUtilities.saveChartAsPNG(file, chart, 400, 400);
        } catch (IOException e){
            System.out.println("ERROR AL GUARDAR LA IMAGEN");
            e.printStackTrace();
        }
        return pathPicture;
    }



}
