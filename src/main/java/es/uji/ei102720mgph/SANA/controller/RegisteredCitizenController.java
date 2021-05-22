package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.RegisteredCitizenDao;
import es.uji.ei102720mgph.SANA.dao.SanaUserDao;
import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/registeredCitizen")
public class RegisteredCitizenController {

    private RegisteredCitizenDao registeredCitizenDao;
    private SanaUserDao sanaUserDao;

    @Autowired
    public void setRegisteredCitizenDao(RegisteredCitizenDao registeredCitizenDao){
        this.registeredCitizenDao = registeredCitizenDao;
    }

    @Autowired
    public void setSanaUserDao(SanaUserDao sanaUserDao){
        this.sanaUserDao = sanaUserDao;
    }

    // Operació crear
    @RequestMapping(value="/add")
    public String addRegisteredCitizen(Model model) {
        model.addAttribute("registeredCitizen", new RegisteredCitizen());
        return "registeredCitizen/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("registeredCitizen")RegisteredCitizen registeredCitizen,
                                   BindingResult bindingResult) {
        // si ha saltado una excepción, quitar el atributo
        if(model.getAttribute("emailRepetido") != null)
            model.addAttribute("emailRepetido", null);
        if(model.getAttribute("NIERepetido") != null)
            model.addAttribute("NIERepetido", null);
        if(model.getAttribute("telfRepetido") != null)
            model.addAttribute("telfRepetido", null);

        if (bindingResult.hasErrors())
            return "registeredCitizen/add";
        try {
            registeredCitizenDao.addRegisteredCitizen(registeredCitizen);
        } catch (DataIntegrityViolationException e) {
            // alguna clave primaria o alternativa repetida, ver cuál es
            if(sanaUserDao.getSanaUser(registeredCitizen.getEmail()) != null)
                model.addAttribute("emailRepetido", "repetido");
            sanaUserDao.deleteSanaUser(registeredCitizen.getEmail());
            if(registeredCitizenDao.getRegisteredCitizenNIE(registeredCitizen.getIdNumber()) != null)
                model.addAttribute("NIERepetido", "repetido");
            if(registeredCitizenDao.getRegisteredCitizenTelf(registeredCitizen.getMobilePhoneNumber()) != null)
                model.addAttribute("telfRepetido", "repetido");
            return "registeredCitizen/add";
        }
        return "redirect:list";
    }
}
