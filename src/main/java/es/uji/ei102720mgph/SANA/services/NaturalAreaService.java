package es.uji.ei102720mgph.SANA.services;

import es.uji.ei102720mgph.SANA.model.NaturalArea;

import java.util.List;
import java.util.Map;

public interface NaturalAreaService {
    Map<NaturalArea, List<String>> getNaturalAreasWithImages();
    Map<NaturalArea, String> getNaturalAreasWithImage();

}
