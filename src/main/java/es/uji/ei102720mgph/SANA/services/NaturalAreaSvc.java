package es.uji.ei102720mgph.SANA.services;

import es.uji.ei102720mgph.SANA.dao.NaturalAreaDao;
import es.uji.ei102720mgph.SANA.dao.PictureDao;
import es.uji.ei102720mgph.SANA.model.NaturalArea;
import es.uji.ei102720mgph.SANA.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

@Service
public class NaturalAreaSvc implements NaturalAreaService  {
    @Autowired
    NaturalAreaDao naturalAreaDao;

    @Autowired
    PictureDao pictureDao;

    // Este metodo obtiene todas las areas naturales con todas sus imagenes
    @Override
    public Map<NaturalArea, List<String>> getNaturalAreasWithImages() {
        List<NaturalArea> naturalAreas = naturalAreaDao.getNaturalAreas();
        HashMap<NaturalArea, List<String>> imagesOfNaturalAreas = new HashMap<NaturalArea,List<String >>();
        for (NaturalArea naturalArea : naturalAreas) {
            List<Picture> pictures = pictureDao.getPicturesOfNaturalArea(naturalArea.getName());
            // De picture solo nos interesa la ruta de la imagen
            List<String> picturesPath = new LinkedList<>();
            for (Picture picture: pictures) picturesPath.add(picture.getPicturePath());
            imagesOfNaturalAreas.put(naturalArea, picturesPath);
        }
        return imagesOfNaturalAreas;
    }

    // Este metodo obtiene todas las areas naturales con una sola imagen
    @Override
    public Map<NaturalArea, String> getNaturalAreasWithImage() {
        List<NaturalArea> naturalAreas = naturalAreaDao.getNaturalAreas();
        HashMap<NaturalArea, String> naturalAreasWithImage = new HashMap<NaturalArea, String>();
        for (NaturalArea naturalArea : naturalAreas) {
            // Seleccionamos la primera imagen (si no tiene ninguna -> picture = null)
            Picture picture = pictureDao.getPicturesOfNaturalArea(naturalArea.getName()).get(0);
            // De picture solo nos interesa la ruta de la imagen
            naturalAreasWithImage.put(naturalArea, picture.getPicturePath());
        }
        return naturalAreasWithImage;
    }

}
