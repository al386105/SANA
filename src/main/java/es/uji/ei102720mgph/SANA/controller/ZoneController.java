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
    @RequestMapping(value="/add")
    public String addZone(Model model) {
        model.addAttribute("zone", new Zone());
        return "zone/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("zone") Zone zone,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "zone/add"; //tornem al formulari per a que el corregisca
        zoneDao.addZone(zone); //usem el dao per a inserir el zone
        return "redirect:list"; //redirigim a la lista per a veure el zone afegit, post/redirect/get
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{zoneNumber}/{letter}", method = RequestMethod.GET)
    public String editZone(Model model, @PathVariable int zoneNumber, @PathVariable char letter) {
        model.addAttribute("zone", zoneDao.getZone(zoneNumber, letter));
        return "zone/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("zone") Zone zone,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "zone/update";
        zoneDao.updateZone(zone);
        return "redirect:list";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{zoneNumber}/{letter}")
    public String processDelete(@PathVariable int zoneNumber, @PathVariable char letter) {
        zoneDao.deleteZone(zoneNumber, letter);
        return "redirect:../list";
    }
}

