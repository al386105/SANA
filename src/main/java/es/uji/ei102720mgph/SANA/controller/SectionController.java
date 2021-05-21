package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.UserLogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/section")
public class SectionController {

    @RequestMapping("/managers")
    public String sectionManagers(Model model, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            return "/inicio/login";
        }
        return "section/managers";
    }

    @RequestMapping("/environmentalManager")
    public String sectionEnvironmentalmanager(Model model, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            return "/inicio/login";
        }
        return "section/environmentalManager";
    }
}
