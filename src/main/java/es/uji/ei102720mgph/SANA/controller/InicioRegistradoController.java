package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.RegisteredCitizenDao;
import es.uji.ei102720mgph.SANA.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/inicio/registrado")
public class InicioRegistradoController {

    private RegisteredCitizenDao registeredCitizenDao;

    @Autowired
    public void setRegisteredCitizenDao(RegisteredCitizenDao registeredCitizenDao){
        this.registeredCitizenDao = registeredCitizenDao;
    }

    @RequestMapping("/perfil")
    public String redirigirRegistradoPerfil(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicio/registrado/perfil");
            return "redirect:/inicio/login";
        }
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        model.addAttribute("citizen", citizen);
        model.addAttribute("citizenName", citizen.getName());
        return "inicioRegistrado/perfil";
    }

    @RequestMapping("/editarPerfil")
    public String redirigirRegistradoEditarPerfil(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicioRegistrado/editarPerfil");
            return "redirect:/inicio/login";
        }
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        model.addAttribute("citizenName", citizen.getName());
        model.addAttribute("citizen", citizen);
        return "inicioRegistrado/editarPerfil";
    }

    @RequestMapping(value="/editarPerfil", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("citizen") RegisteredCitizen registeredCitizen,
                                      BindingResult bindingResult, Model model, HttpSession session) {
        PerfilValidator perfilValidator = new PerfilValidator();
        perfilValidator.validate(registeredCitizen, bindingResult);
        if (bindingResult.hasErrors())
            return "inicio/registrado/editarPerfil";
        RegisteredCitizen oldRegisteredCitizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        registeredCitizen.setCitizenCode(oldRegisteredCitizen.getCitizenCode());
        registeredCitizenDao.updateRegisteredCitizen(registeredCitizen);
        model.addAttribute("citizen", registeredCitizen);
        model.addAttribute("citizenName", registeredCitizen.getName());
        session.setAttribute("registeredCitizen", registeredCitizen);
        return "redirect:perfil";
    }

    @RequestMapping(value="/welcome")
    public String welcome(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null){
            model.addAttribute("userLogin", new UserLogin() {});
            return "redirect:/inicio/login";
        }
        RegisteredCitizen registeredCitizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        model.addAttribute("username", registeredCitizen.getName());
        model.addAttribute("citizenName", registeredCitizen.getName());
        return "inicioRegistrado/welcome";
    }
}
