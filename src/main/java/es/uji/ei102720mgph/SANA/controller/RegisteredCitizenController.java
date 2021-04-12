package es.uji.ei102720mgph.SANA.controller;


import es.uji.ei102720mgph.SANA.dao.RegisteredCitizenDao;
import es.uji.ei102720mgph.SANA.model.Address;
import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/registeredCitizen")
public class RegisteredCitizenController {

    private RegisteredCitizenDao registeredCitizenDao;

    @Autowired
    public void setRegisteredCitizenDao(RegisteredCitizenDao registeredCitizenDao){
        this.registeredCitizenDao = registeredCitizenDao;
    }

    @RequestMapping("/list")
    public String listRegisteredCitizens(Model model){
        model.addAttribute("registeredCitizens", registeredCitizenDao.getRegisteredCitizens());
        return "registeredCitizen/list";
    }

    // Operació crear
    @RequestMapping(value="/add")
    public String addRegisteredCitizen(Model model) {
        model.addAttribute("registeredCitizen", new RegisteredCitizen());
        return "registeredCitizen/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("registeredCitizen")RegisteredCitizen registeredCitizen,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "registeredCitizen/add"; //tornem al formulari per a que el corregisca
        registeredCitizenDao.addRegisteredCitizen(registeredCitizen); //usem el dao per a inserir el address
        return "redirect:list"; //redirigim a la lista per a veure el address afegit, post/redirect/get
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{email}", method = RequestMethod.GET)
    public String editRegisteredCitizen(Model model, @PathVariable String email) {
        model.addAttribute("registeredCitizen", registeredCitizenDao.getRegisteredCitizen(email));
        return "registeredCitizen/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("registeredCitizen") RegisteredCitizen registeredCitizen,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "registeredCitizen/update";
        registeredCitizenDao.updateRegisteredCitizen(registeredCitizen);
        return "redirect:list";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{email}")
    public String processDelete(@PathVariable String email) {
        registeredCitizenDao.deleteRegisteredCitizen(email);
        return "redirect:../list";
    }

}
