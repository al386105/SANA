package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.MunicipalManagerDao;
import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/municipalManager")
public class MunicipalManagerController {

    private MunicipalManagerDao municipalManagerDao;

    @Autowired
    public void setMunicipalManagerDao(MunicipalManagerDao mmd) {
        this.municipalManagerDao=mmd;
    }

    @RequestMapping("/list")
    public String listMunicipalManager(Model model) {
        model.addAttribute("municipalManagers", municipalManagerDao.getMunicipalManagers());
        return "municipalManager/list";
    }

    @RequestMapping(value="/add")
    public String addMunicipalManager(Model model) {
        model.addAttribute("municipalManager", new MunicipalManager());
        return "municipalManager/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("municipalManager") MunicipalManager manager,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "municipalManager/add"; //tornem al formulari per a que el corregisca
        municipalManagerDao.addMunicipalManager(manager); //usem el dao per a inserir el reservation
        return "redirect:list"; //redirigim a la lista per a veure el reservation afegit, post/redirect/get
    }

    @RequestMapping(value="/update/{email}", method = RequestMethod.GET)
    public String editMunicipalManager(Model model, @PathVariable String email) {
        model.addAttribute("municipalManager", municipalManagerDao.getMunicipalManager(email));
        return "municipalManager/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("municipalManager") MunicipalManager manager,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "municipalManager/update";
        municipalManagerDao.updateMunicipalManager(manager);
        return "redirect:list";
    }

    @RequestMapping(value="/delete/{email}")
    public String processDelete(@PathVariable String email) {
        municipalManagerDao.deleteMunicipalManager(email);
        return "redirect:../list";
    }
}
