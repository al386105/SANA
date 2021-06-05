package es.uji.ei102720mgph.SANA.services;

import es.uji.ei102720mgph.SANA.model.OccupancyData;
import es.uji.ei102720mgph.SANA.model.NaturalArea;

import java.time.LocalDate;
import java.util.List;

public interface OccupationService {

    List<OccupancyData> getOccupancyDataOfNaturalAreas(List<NaturalArea> naturalAreas);
    String getOccupancyPlotByYear(String naturalArea, int year);
    String getOccupancyPlotByMonth(String naturalArea, int year, int month);
    String getOccupancyPlotByDay(String naturalArea, LocalDate day);
    public String getMunicipalitiesPlot();
    public String getMunicipalityPlot(String municipality);

    }
