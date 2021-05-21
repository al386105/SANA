package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.EmailDao;
import es.uji.ei102720mgph.SANA.dao.MunicipalManagerDao;
import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.dao.SanaUserDao;
import es.uji.ei102720mgph.SANA.model.Email;
import es.uji.ei102720mgph.SANA.model.MunicipalManager;
import es.uji.ei102720mgph.SANA.model.Municipality;
import es.uji.ei102720mgph.SANA.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
