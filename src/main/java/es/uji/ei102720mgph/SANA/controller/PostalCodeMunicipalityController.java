package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.MunicipalManagerDao;
import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.dao.PostalCodeMunicipalityDao;
import es.uji.ei102720mgph.SANA.model.PostalCodeMunicipality;
import es.uji.ei102720mgph.SANA.model.UserLogin;
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
@RequestMapping("/postalCode")
public class PostalCodeMunicipalityController {
    private MunicipalityDao municipalityDao;
    private MunicipalManagerDao municipalManagerDao;
    private PostalCodeMunicipalityDao postalCodeMunicipalityDao;

    @Autowired
    public void setMunicipalityDao(MunicipalityDao municipalityDao){
        this.municipalityDao = municipalityDao;
    }

    @Autowired
    public void setMunicipalManagerDao(MunicipalManagerDao municipalManagerDao){
        this.municipalManagerDao = municipalManagerDao;
    }

    @Autowired
    public void setPostalCodeMunicipalityDao(PostalCodeMunicipalityDao postalCodeMunicipalityDao){
        this.postalCodeMunicipalityDao = postalCodeMunicipalityDao;
    }


    private PostalCodeMunicipalityDao pcD;

    @Autowired
    public void setPostalCodeDao(PostalCodeMunicipalityDao postalCodeDao){
        this.pcD = postalCodeDao;
    }

    @RequestMapping(value="/add")
    public String addPostalCode(Model model, @PathVariable String municipality, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/postalCode/add");
            return "redirect:/inicio/login";
        }
        model.addAttribute("postalCode", new PostalCodeMunicipality());
        session.setAttribute("section", "#postalCodes");
        return "postalCode/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("postalCode") PostalCodeMunicipality pc,
                                   BindingResult bindingResult) {
        PostalCodeMunicipalityValidator postalCodeMunicipalityValidator = new PostalCodeMunicipalityValidator();
        postalCodeMunicipalityValidator.validate(pc, bindingResult);
        if(model.getAttribute("claveRepetida") != null)
            model.addAttribute("claveRepetida", null);

        String municipality = pc.getMunicipality();
        if (bindingResult.hasErrors()) {
            recargaGet(model, municipality);
            return "/municipality/get/" + municipality; //TODO NO VA
        }
        try {
            pcD.addPostalCode(pc);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("claveRepetida", "repetida");
            recargaGet(model, municipality);
            return "/municipality/get/" + municipality; //TODO NO VA
        }
        return "redirect:/municipality/get/" + municipality;
    }

    public void recargaGet(Model model, String name) {
        model.addAttribute("municipality", municipalityDao.getMunicipality(name));
        model.addAttribute("municipalManagers", municipalManagerDao.getManagersOfMunicipality(name));
        model.addAttribute("postalCodes", postalCodeMunicipalityDao.getPostalCodeOfMuni(name));
    }

    @RequestMapping(value="/delete/{municipality}/{postalCode}")
    public String processDelete(Model model, @PathVariable String municipality,@PathVariable String postalCode, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/postalCode/delete/" + municipality + "/" + postalCode);
            return "redirect:/inicio/login";
        }
        pcD.deletePostalCode(municipality, postalCode);
        session.setAttribute("section", "#postalCodes");
        return "redirect:/municipality/get/" + municipality;
    }
}
