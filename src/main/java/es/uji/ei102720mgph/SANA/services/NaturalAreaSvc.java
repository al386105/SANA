package es.uji.ei102720mgph.SANA.services;

import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.dao.NaturalAreaDao;
import es.uji.ei102720mgph.SANA.dao.PictureDao;
import es.uji.ei102720mgph.SANA.model.NaturalArea;
import es.uji.ei102720mgph.SANA.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NaturalAreaSvc implements NaturalAreaService  {
    @Autowired
    NaturalAreaDao naturalAreaDao;

    @Autowired
    PictureDao pictureDao;

    @Autowired
    MunicipalityDao municipalityDao;


    // Este metodo obtiene todas las areas naturales con una sola imagen
    @Override
    public SortedMap<NaturalArea, String> getNaturalAreasWithImage() {
        List<NaturalArea> naturalAreas = naturalAreaDao.getNaturalAreas();
        SortedMap<NaturalArea, String> naturalAreasWithImage = new TreeMap<NaturalArea, String>();
        for (NaturalArea naturalArea : naturalAreas) {
            // Seleccionamos la primera imagen (si no tiene ninguna -> picture = null)
            Picture picture = pictureDao.getPicturesOfNaturalArea(naturalArea.getName()).get(0);
            // De picture solo nos interesa la ruta de la imagen
            naturalAreasWithImage.put(naturalArea, picture.getPicturePath());
        }
        return naturalAreasWithImage;
    }

    /**
     * Este metodo devuelve un array con la ruta a una imagen de naturalArea.
     * La ruta de la imagen pictures[0] corresponde a la naturalAreas[0],
     * pictures[1] corresponde a naturalAreas[1] y así sucesivamente
     * Devuelve la primera imagen asociada de cada naturalArea
     * Si no tienen imagen asociada, en su respectiva posicion será null
     */
    public List<String> getImageOfNaturalAreas(List<NaturalArea> naturalAreas){
        ArrayList<String> pictures = new ArrayList<>(naturalAreas.size());
        for (int i = 0; i < naturalAreas.size(); i++){
            List<Picture> picturesOfNaturalArea = pictureDao.getPicturesOfNaturalArea(naturalAreas.get(i).getName());
            if (picturesOfNaturalArea.size() > 0){
                pictures.add(picturesOfNaturalArea.get(0).getPicturePath());
            }
            else{
                //Si no tiene imagenes, añadimos una imagen por defecto asociada
                pictures.add("/assets/img/naturalAreas/noPicture.png");

            }
        }
        return pictures;
    }


    public List<String> getNameOfNaturalAreas(){
        List<NaturalArea> naturalAreas = naturalAreaDao.getNaturalAreas();
        ArrayList<String> nameOfNaturalAreas = new ArrayList<>(naturalAreas.size());
        for(NaturalArea naturalArea: naturalAreas){
            nameOfNaturalAreas.add(naturalArea.getName());
        }
        return nameOfNaturalAreas;
    }

    public List<String> getNameOfRestrictedNaturalAreas(){
        List<NaturalArea> naturalAreas = naturalAreaDao.getRestrictedNaturalAreas();
        ArrayList<String> nameOfNaturalAreas = new ArrayList<>(naturalAreas.size());
        for(NaturalArea naturalArea: naturalAreas){
            nameOfNaturalAreas.add(naturalArea.getName());
        }
        return nameOfNaturalAreas;
    }

}
