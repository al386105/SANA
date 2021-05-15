package es.uji.ei102720mgph.SANA.services;

import es.uji.ei102720mgph.SANA.model.NaturalArea;

import java.util.List;
import java.util.SortedMap;

public interface NaturalAreaService {
    SortedMap<NaturalArea, String> getNaturalAreasWithImage();
    List<String> getImageOfNaturalAreas(List<NaturalArea> naturalAreas);
    List<String> getNameOfNaturalAreas();
    List<String> getNameOfRestrictedNaturalAreas();
}
