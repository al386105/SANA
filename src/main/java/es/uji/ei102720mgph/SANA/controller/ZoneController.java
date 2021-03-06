package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.ZoneDao;
import es.uji.ei102720mgph.SANA.model.UserLogin;
import es.uji.ei102720mgph.SANA.model.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/zone")
public class ZoneController {
    private ZoneDao zoneDao;

    @Autowired
    public void setZoneDao(ZoneDao zoneDao) {
        this.zoneDao=zoneDao;
    }

    // Operació crear
    @RequestMapping(value="/add/{naturalArea}")
    public String addZone(Model model, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/zone/add/" + naturalArea);
            return "redirect:/inicio/login";
        }
        Zone zone = new Zone();
        zone.setNaturalArea(naturalArea);
        model.addAttribute("zone", zone);
        model.addAttribute("naturalArea", naturalArea);
        return "zone/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("zone") Zone zone, BindingResult bindingResult) {
        ZoneValidator zoneValidator = new ZoneValidator();
        zoneValidator.validate(zone, bindingResult);
        if (bindingResult.hasErrors())
            return "zone/add"; //tornem al formulari per a que el corregisca
        if(zoneDao.getZone(zone.getZoneNumber(), zone.getLetter()) != null) {
            bindingResult.rejectValue("letter", "repetido", "La combinación de número y letra ya está en uso");
            return "zone/add";
        }
        String naturalAreaName = zone.getNaturalArea();
        return "redirect:/zone/getForManagersZones/" + naturalAreaName;
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editZone(Model model, @PathVariable String id, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/zone/update/" + id);
            return "redirect:/inicio/login";
        }
        model.addAttribute("zone", zoneDao.getZone(id));
        return "zone/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(Model model, @ModelAttribute("zone") Zone zone, BindingResult bindingResult,
                                      HttpSession session) {
        ZoneValidator zoneValidator = new ZoneValidator();
        zoneValidator.validate(zone, bindingResult);
        if (bindingResult.hasErrors())
            return "zone/update";
        zoneDao.updateZone(zone);
        return "redirect:/zone/getForManagersZones/" + zone.getNaturalArea();
    }

    // Operacio de llistar totes les zones d'un area natural
    @RequestMapping("/porNaturalArea/{naturalArea}")
    public String listZonesOfNaturalArea(Model model, @PathVariable String naturalArea) {
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        return "zone/porNaturalArea";
    }

    @RequestMapping(value="/getForManagersZones/{naturalArea}")
    public String getZonesForManagers(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/zone/getForManagersZones/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalArea);
        model.addAttribute("zones", zoneDao.getZonesOfNaturalArea(naturalArea));
        return "/zone/getForManagersZones";
    }
}

