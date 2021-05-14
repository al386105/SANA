package es.uji.ei102720mgph.SANA.services;

import es.uji.ei102720mgph.SANA.model.NaturalArea;

import java.time.LocalDate;
import java.util.List;
import java.util.SortedMap;

public interface OccupationService {
    float getRateDayOccupancyOfNaturalArea(String naturalArea, LocalDate date);

}
