package es.uji.ei102720mgph.SANA.services;

import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.dao.NaturalAreaDao;
import es.uji.ei102720mgph.SANA.enums.Months;
import es.uji.ei102720mgph.SANA.model.*;
import es.uji.ei102720mgph.SANA.dao.ReservationDao;
import es.uji.ei102720mgph.SANA.dao.ZoneDao;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OccupationSvc implements OccupationService{
    @Value("${upload.file.directory}")
    private String uploadDirectory;

    @Autowired
    NaturalAreaDao naturalAreaDao;

    @Autowired
    MunicipalityDao municipalityDao;

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    ZoneDao zoneDao;

    private int getOccupancy(List<Reservation> reservations){
        int occupancy = 0;
        for(Reservation reservation: reservations){
            if(reservation.getState().getDescripcion().equals("Creada") ||
                    reservation.getState().getDescripcion().equals("Usada") ||
                    reservation.getState().getDescripcion().equals("En uso")){
                occupancy += reservation.getNumberOfPeople();
            }
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

    private void saveChart(File file, JFreeChart chart){
        //Guardamos la imagen
        try {
            ChartUtilities.saveChartAsPNG(file, chart, 700, 400);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getOccupancyPlotByYear(String naturalArea, int year) {
        String plotName = naturalArea + year + ".png";
        String path = uploadDirectory + "plots/" + plotName;

        File file = new File(path);

        //Generamos el dataSet:
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate date;
        int occupancy;
        int months = 12;
        for(int month = 1; month <= months; month++){
            date = LocalDate.of(year, month, 1);
            occupancy = getOccupancy(reservationDao.getReservationsOfNaturalAreaOfMonth(naturalArea, date));
            dataset.addValue(occupancy, naturalArea, Months.values()[month - 1].getDescripcion());
        }

        //Generamos el chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Ocupación en " + naturalArea + " durante " + year,
                "Mes",
                "Número de reservas",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        //Establecemos valores enteros en el eje y
        CategoryPlot plot = chart.getCategoryPlot();
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        saveChart(file, chart);

        return  "plots/" + file.getName();
    }

    public String getOccupancyPlotByMonth(String naturalArea, int year, int month) {
        String plotName = naturalArea + year + "-" + month + ".png";
        String path = uploadDirectory + "plots/" + plotName;

        File file = new File(path);

        //Generamos el dataSet:
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate date = LocalDate.of(year, month, 1);
        int occupancy;
        int days = date.lengthOfMonth();
        for(int day = 1; day <= days; day++){
            date = LocalDate.of(year, month, day);
            occupancy = getOccupancy(
                    reservationDao.getReservationsOfNaturalAreaOfDay(naturalArea, date));
            dataset.addValue(occupancy, naturalArea, day  + "");
        }

        //Generamos el chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Ocupación en " + naturalArea + " durante " + month + "/" + year,
                "Dia",
                "Número de reservas",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        //Establecemos valores enteros en el eje y
        CategoryPlot plot = chart.getCategoryPlot();
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        saveChart(file, chart);

        return "plots/" + plotName;
    }

    public String getOccupancyPlotByDay(String naturalArea, LocalDate day) {
        String plotName = naturalArea + day + ".png";
        String path = uploadDirectory + "plots/" + plotName;

        File file = new File(path);

        //Generamos el dataSet:
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalTime time;
        int occupancy;
        int hours = 24;
        for(int hour = 0; hour < hours; hour++){
            time =  LocalTime.of(hour, 1);
            occupancy = getOccupancy(
                    reservationDao.getReservationsOfNaturalAreaOfHour(naturalArea, day, time));
            dataset.addValue(occupancy, naturalArea, hour  + "");
        }

        //Generamos el chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Ocupación en " + naturalArea + " durante el día " + day.toString(),
                "Hora",
                "Número de reservas",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        //Establecemos valores enteros en el eje y
        CategoryPlot plot = chart.getCategoryPlot();
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        saveChart(file, chart);

        return "plots/" + plotName;
    }

    public String getMunicipalitiesPlot() {
        String plotName = "municipalities.png";
        String path = uploadDirectory + "plots/" + plotName;

        File file = new File(path);

        List<Municipality> municipalities = municipalityDao.getMunicipalities();

        //Generamos el dataSet:
        DefaultPieDataset dataset = new DefaultPieDataset();
        int occupancy;

        for(Municipality municipality: municipalities){
            occupancy = getOccupancy(
                    reservationDao.getReservationsOfMunicipality(municipality.getName()));
            if (occupancy > 0) dataset.setValue(municipality.getName(), occupancy);
        }

        //Generamos el chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Accesos totales a las areas naturalaes de los distintos municipios",
                dataset,
                false, true, false);

        saveChart(file, chart);

        return "plots/" + plotName;
    }

    public String getMunicipalityPlot(String municipality){
        String plotName = "municipality.png";
        String path = uploadDirectory + "plots/" + plotName;

        File file = new File(path);

        List<NaturalArea> naturalAreas = naturalAreaDao.getRestrictedNaturalAreas();


        //Generamos el dataSet:
        DefaultPieDataset dataset = new DefaultPieDataset();
        int occupancy;

        for(NaturalArea naturalArea: naturalAreas){
            if (naturalArea.getMunicipality().equals(municipality)){
                occupancy = getOccupancy(
                        reservationDao.getReservationsOfNaturalArea(naturalArea.getName()));
                if (occupancy > 0) dataset.setValue(naturalArea.getName(), occupancy);
            }

        }

        //Generamos el chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Accesos totales en las distintas areas naturales de " + municipality,
                dataset,
                false, true, false);

        saveChart(file, chart);

        return "plots/" + plotName;
    }

}
