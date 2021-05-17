package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.SanaUserDao;
import es.uji.ei102720mgph.SANA.dao.ZoneDao;
import es.uji.ei102720mgph.SANA.dao.ZoneRowMapper;
import es.uji.ei102720mgph.SANA.model.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ZoneValidator implements Validator {
    private ZoneDao zoneDao;

    @Override
    public boolean supports(Class<?> cls) {
        return Zone.class.equals(cls);
    }

    @Autowired
    public void setZoneDao(ZoneDao zoneDao) { this.zoneDao = zoneDao; }

    @Override
    public void validate(Object obj, Errors errors) {
        Zone zone = (Zone)obj;

        // Letra obligatoria
        if (zone.getLetter().trim().equals(""))
            errors.rejectValue("letter", "obligatorio", "Es obligatorio completar la letra");

        // TODO NO VA
        // Si ya existe una zona con ese numero y letra...
        /*if(zoneDao.getZone(zone.getZoneNumber(), zone.getLetter()) != null)
            errors.rejectValue("letter", "repetido", "La combinación de número y letra ya existe para otra zona");*/
    }
}
