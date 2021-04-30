package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ZoneDao;
import es.uji.ei102720mgph.SANA.model.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/zone")
public class ZoneController {

    private ZoneDao zoneDao;

    @Autowired
    public void setZoneDao(ZoneDao zoneDao) {
        this.zoneDao=zoneDao;
    }

    // Operació llistar
    @RequestMapping("/list")
    public String listZones(Model model) {
        model.addAttribute("zones", zoneDao.getZones());
        return "zone/list";
    }

    // Operació crear
    @RequestMapping(value="/add/{naturalArea}")
    public String addZone(Model model, @PathVariable String naturalArea) {
        Zone zone = new Zone();
        zone.setNaturalArea(naturalArea);
        model.addAttribute("zone", zone);
        return "zone/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("zone") Zone zone,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "zone/add"; //tornem al formulari per a que el corregisca
        String naturalAreaName = zone.getNaturalArea();
        zoneDao.addZone(zone);
        return "redirect:/naturalArea/getManagers/" + naturalAreaName; //redirigim a la lista per a veure el zone afegit
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editZone(Model model, @PathVariable String id) {
        model.addAttribute("zone", zoneDao.getZone(id));
        return "zone/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("zone") Zone zone,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "zone/update";
        String naturalAreaName = zone.getNaturalArea();
        zoneDao.updateZone(zone);
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }

    // Operacio de llistar totes les zones d'un area natural
    @RequestMapping("/porNaturalArea/{naturalArea}")
    public String listZonesOfNaturalArea(Model model, @PathVariable String naturalArea) {
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        return "zone/porNaturalArea";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable String id) {
        Zone zone = zoneDao.getZone(id);
        String naturalAreaName = zone.getNaturalArea();
        zoneDao.deleteZone(id);
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }
}

