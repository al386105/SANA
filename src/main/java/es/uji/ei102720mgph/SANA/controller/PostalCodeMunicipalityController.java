package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.MunicipalManagerDao;
import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.dao.PostalCodeMunicipalityDao;
import es.uji.ei102720mgph.SANA.model.PostalCodeMunicipality;
import es.uji.ei102720mgph.SANA.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private PostalCodeMunicipalityDao pcD;

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

    @Autowired
    public void setPostalCodeDao(PostalCodeMunicipalityDao postalCodeDao){
        this.pcD = postalCodeDao;
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("postalCode") PostalCodeMunicipality pc,
                                   BindingResult bindingResult, HttpSession session) {
        PostalCodeMunicipalityValidator postalCodeMunicipalityValidator = new PostalCodeMunicipalityValidator();
        postalCodeMunicipalityValidator.validate(pc, bindingResult);

        String municipality = pc.getMunicipality();
        if (bindingResult.hasErrors()) {
            session.setAttribute("incorrecto", "Formato incorrecto. Deben ser 5 dígitos numéricos.");
            return "redirect:/municipality/get/" + municipality;
        } try {
            pcD.addPostalCode(pc);
        } catch (DataIntegrityViolationException e) {
            session.setAttribute("claveRepetida", "El código postal ya existe");
            return "redirect:/municipality/get/" + municipality;
        }
        return "redirect:/municipality/get/" + municipality;
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
