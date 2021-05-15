package es.uji.ei102720mgph.SANA.services;

import es.uji.ei102720mgph.SANA.model.OccupancyData;
import es.uji.ei102720mgph.SANA.model.NaturalArea;

import java.time.LocalDate;
import java.util.List;

public interface OccupationService {
    float getRateDayOccupancyOfNaturalArea(String naturalArea, LocalDate date);
    int getOccupancyOfDay(String naturalArea, LocalDate date);
    int getTotalOccupancy(String naturalArea);
    int getMaxCapacityOfNaturalArea(String naturalArea);
    List<OccupancyData> getOccupancyDataOfNaturalAreas(List<NaturalArea> naturalAreas);

    }
