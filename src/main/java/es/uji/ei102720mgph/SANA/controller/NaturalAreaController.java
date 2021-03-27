package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.NaturalAreaDao;
import es.uji.ei102720mgph.SANA.model.NaturalArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/naturalArea")
public class NaturalAreaController {
    private NaturalAreaDao naturalAreaDao;

    @Autowired
    public void setNaturalAreaDao(NaturalAreaDao naturalAreaDao){ this.naturalAreaDao = naturalAreaDao; }

    @RequestMapping("/list")
    public String listNaturalAreas(Model model){
        model.addAttribute("naturalAreas", naturalAreaDao.getNaturalAreas());
        return "naturalArea/list";
    }

    @RequestMapping(value="/add")
    public String addNaturalArea(Model model) {
        model.addAttribute("naturalArea", new NaturalArea());
        return "naturalArea/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("naturalArea") NaturalArea naturalArea,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "naturalArea/add"; //tornem al formulari per a que el corregisca
        naturalAreaDao.addNaturalArea(naturalArea); //usem el dao per a inserir el naturalArea
        return "redirect:list"; //redirigim a la lista per a veure la naturalArea afegida, post/redirect/get
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{naturalArea}", method = RequestMethod.GET)
    public String editNaturalArea(Model model, @PathVariable String naturalArea) {
        model.addAttribute("naturalArea", naturalAreaDao.getNaturalArea(naturalArea));
        return "naturalArea/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("naturalArea") NaturalArea naturalArea,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "naturalArea/update";
        naturalAreaDao.updateNaturalArea(naturalArea);
        return "redirect:list";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{naturalArea}")
    public String processDelete(@PathVariable String naturalArea) {
        naturalAreaDao.deleteNaturalArea(naturalArea);
        return "redirect:../list";
    }
}
