package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.UserLogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/environmentalManager")
public class EnvironmentalManagerController {

    @RequestMapping("home")
    public String sectionEnvironmentalManager(Model model, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            return "/inicio/login";
        }
        return "environmentalManager/home";
    }

}
