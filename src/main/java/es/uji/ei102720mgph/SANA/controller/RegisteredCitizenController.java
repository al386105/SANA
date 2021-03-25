package es.uji.ei102720mgph.SANA.controller;


import es.uji.ei102720mgph.SANA.dao.RegisteredCitizenDao;
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
        model.addAttribute("redisteredCitizens", registeredCitizenDao.getRegisteredCitizens());
        return "registeredCitizen/list";
    }

}
