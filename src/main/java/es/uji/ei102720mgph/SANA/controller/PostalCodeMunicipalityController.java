package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.PostalCodeMunicipalityDao;
import es.uji.ei102720mgph.SANA.model.PostalCodeMunicipality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/postalCode")
public class PostalCodeMunicipalityController {

    private PostalCodeMunicipalityDao pcD;

    @Autowired
    public void setPostalCodeDao(PostalCodeMunicipalityDao postalCodeDao){
        this.pcD = postalCodeDao;
    }

    @RequestMapping(value="/add")
    public String addPostalCode(Model model, @PathVariable String municipality) {
        model.addAttribute("postalCode", new PostalCodeMunicipality());
        return "postalCode/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("postalCode") PostalCodeMunicipality pc, BindingResult bindingResult) {
        PostalCodeMunicipalityValidator postalCodeMunicipalityValidator = new PostalCodeMunicipalityValidator();
        postalCodeMunicipalityValidator.validate(pc, bindingResult);

        String municipality = pc.getMunicipality();
        //TODO e aqui el fallo
        if (bindingResult.hasErrors())
            return "/municipality/get/" + municipality;
        pcD.addPostalCode(pc);
        return "redirect:/municipality/get/" + municipality;
    }

    @RequestMapping(value="/delete/{municipality}/{postalCode}")
    public String processDelete(@PathVariable String municipality,@PathVariable String postalCode) {
        pcD.deletePostalCode(municipality, postalCode);
        return "redirect:/municipality/get/" + municipality;
    }
}
