package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.MunicipalManagerDao;
import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.dao.PostalCodeMunicipalityDao;
import es.uji.ei102720mgph.SANA.model.Municipality;
import es.uji.ei102720mgph.SANA.model.PostalCodeMunicipality;
import es.uji.ei102720mgph.SANA.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/municipality")
public class MunicipalityController {

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

    @RequestMapping("/list")
    public String listMunicipalities(Model model, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipality/list");
            return "redirect:/inicio/login";
        }
        model.addAttribute("municipalities", municipalityDao.getMunicipalities());
        quitarAtributoSeccion(session);
        return "municipality/list";
    }

    @RequestMapping(value="/get/{name}")
    public String getMunicipality(Model model, @PathVariable("name") String name, HttpSession session){
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipality/get/" + name);
            return "redirect:/inicio/login";
        }
        model.addAttribute("municipality", municipalityDao.getMunicipality(name));
        model.addAttribute("municipalManagers", municipalManagerDao.getManagersOfMunicipality(name));
        model.addAttribute("postalCodes", postalCodeMunicipalityDao.getPostalCodeOfMuni(name));
        PostalCodeMunicipality postalCode = new PostalCodeMunicipality();
        postalCode.setMunicipality(name);
        model.addAttribute("postalCode", postalCode);
        if(session.getAttribute("section") != null) {
            String section = (String) session.getAttribute("section");
            session.removeAttribute("section");
            return "redirect:/municipality/get/" + name + section;
        }
        return "/municipality/get";
    }

    @RequestMapping(value="/add")
    public String addMunicipality(Model model, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipality/add");
            return "redirect:/inicio/login";
        }
        model.addAttribute("municipality", new Municipality());
        return "municipality/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("municipality") Municipality municipality,
                                   BindingResult bindingResult) {
        MunicipalityValidator municipalityValidator = new MunicipalityValidator();
        municipalityValidator.validate(municipality, bindingResult);
        if(model.getAttribute("claveRepetida") != null)
            model.addAttribute("claveRepetida", null);
        if (bindingResult.hasErrors())
            return "municipality/add"; //tornem al formulari per a que el corregisca

        try {
            municipalityDao.addMunicipality(municipality);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("claveRepetida", "repetida");
            return "municipality/add";
        }
        return "redirect:list"; //redirigim a la lista,
    }

    @RequestMapping(value="/update/{name}", method = RequestMethod.GET)
    public String editMunicipality(Model model, @PathVariable String name, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipality/update/" + name);
            return "redirect:/inicio/login";
        }
        model.addAttribute("municipality", municipalityDao.getMunicipality(name));
        quitarAtributoSeccion(session);
        return "municipality/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("municipality") Municipality municipality,
                                      BindingResult bindingResult) {
        MunicipalityValidator municipalityValidator = new MunicipalityValidator();
        municipalityValidator.validate(municipality, bindingResult);

        if (bindingResult.hasErrors())
            return "municipality/update";
        municipalityDao.updateMunicipality(municipality);
        return "redirect:list";
    }

    /*@RequestMapping(value="/delete/{name}")
    public String processDelete(Model model, @PathVariable String name, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipality/delete/" + name);
            return "redirect:/inicio/login";
        }
        municipalityDao.deleteMunicipality(name);
        return "redirect:../list";
    }*/

    private void quitarAtributoSeccion(HttpSession session) {
        if(session.getAttribute("section") != null)
            session.removeAttribute("section");
    }
}
