package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.*;
import org.springframework.beans.factory.annotation.Value;
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

    @RequestMapping("/")
    public String redirigirRegistrado(Model model, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/inicioRegistrado/areasNaturales");
            return "redirect:/inicio/login";
        }
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        model.addAttribute("citizen", citizen);
        return "inicioRegistrado/areasNaturales";
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
        model.addAttribute("citizen", registeredCitizen);
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
        return "inicioRegistrado/welcome";
    }
}
