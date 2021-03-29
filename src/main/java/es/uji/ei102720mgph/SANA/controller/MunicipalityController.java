package es.uji.ei102720mgph.SANA.controller;


import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.model.Municipality;
import es.uji.ei102720mgph.SANA.model.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/municipality")
public class MunicipalityController {

    private MunicipalityDao municipalityDao;

    @Autowired
    public void setMunicipalityDao(MunicipalityDao municipalityDao){
        this.municipalityDao = municipalityDao;
    }

    @RequestMapping("/list")
    public String listMunicipalities(Model model) {
        model.addAttribute("municipalities", municipalityDao.getMunicipalities());
        return "municipality/list";
    }

    @RequestMapping(value="/add")
    public String addMunicipality(Model model) {
        model.addAttribute("municipality", new Municipality());
        return "municipality/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("municipality") Municipality muni,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "municipality/add"; //tornem al formulari per a que el corregisca
        municipalityDao.addMunicipality(muni);
        return "redirect:list"; //redirigim a la lista, post/redirect/get
    }

    @RequestMapping(value="/update/{name}", method = RequestMethod.GET)
    public String editMunicipality(Model model, @PathVariable String name) {
        model.addAttribute("municipality", municipalityDao.getMunicipality(name));
        return "municipality/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("municipality") Municipality muni,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "municipality/update";
        municipalityDao.updateMunicipality(muni);
        return "redirect:list";
    }

    @RequestMapping(value="/delete/{name}")
    public String processDelete(@PathVariable String name) {
        municipalityDao.deleteMunicipality(name);
        return "redirect:../list";
    }
}
