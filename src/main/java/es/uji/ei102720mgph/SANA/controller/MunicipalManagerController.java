package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.EmailDao;
import es.uji.ei102720mgph.SANA.dao.MunicipalManagerDao;
import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.dao.SanaUserDao;
import es.uji.ei102720mgph.SANA.model.*;
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
@RequestMapping("/municipalManager")
public class MunicipalManagerController {

    private MunicipalManagerDao municipalManagerDao;
    private EmailDao emailDao;
    private SanaUserDao sanaUserDao;

    @Autowired
    public void setMunicipalManagerDao(MunicipalManagerDao municipalManagerDao) {
        this.municipalManagerDao=municipalManagerDao;
    }

    @Autowired
    public void setSanaUserDao(SanaUserDao sanaUserDao) {
        this.sanaUserDao=sanaUserDao;
    }

    @Autowired
    MunicipalityDao municipalityDao;

    @Autowired
    public void setEmailDao(EmailDao emailDao){
        this.emailDao = emailDao;
    }

    @RequestMapping("/list")
    public String listMunicipalManager(Model model, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipalManager/list");
            return "redirect:/inicio/login";
        }
        model.addAttribute("municipalManagers", municipalManagerDao.getMunicipalManagers());
        if(session.getAttribute("section") != null)
            session.removeAttribute("section");
        return "municipalManager/list";
    }

    // metodo para anyadir al modelo los datos del selector
    @ModelAttribute("municipalityList")
    public List<String> municipalityList() {
        List<Municipality> municipalityList = municipalityDao.getMunicipalities();
        List<String> namesMunicipalities = municipalityList.stream()          // sols els seus noms
                .map(Municipality::getName)
                .collect(Collectors.toList());
        return namesMunicipalities;
    }

    @RequestMapping(value="/add")
    public String addMunicipalManager(Model model, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipalManager/add");
            return "redirect:/inicio/login";
        }
        model.addAttribute("municipalManager", new MunicipalManager());
        return "municipalManager/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("municipalManager") MunicipalManager manager,
                                   BindingResult bindingResult) {
        MunicipalManagerValidator municipalManagerValidator = new MunicipalManagerValidator();
        municipalManagerValidator.validate(manager, bindingResult);

        if (bindingResult.hasErrors())
            return "municipalManager/add"; //tornem al formulari per a que el corregisca
        municipalManagerDao.addMunicipalManager(manager); //usem el dao per a inserir el reservation

        // Enviar mail al nuevo municipal manager
        String destinatario = manager.getEmail();
        String asunto = "Dado de alta";
        String cuerpo = "¡Has sido dado de alta en SANA como gestor municipal en " + manager.getMunicipality() + "! \n" +
                "Tu usuario es " + manager.getUsername() + " y tu contraseña es " + manager.getPassword() + ". \n\n" +
                "SANA. Safe Access to Natural Areas.\nGeneralitat Valenciana";
        AuxiliarController.enviarMail(destinatario, asunto, cuerpo);

        // Anyadir a la tabla de email
        Email email = new Email();
        email.setSanaUser(destinatario);
        email.setSubject(asunto);
        email.setTextBody(cuerpo);
        email.setSender("sana.espais.naturals@gmail.com");
        email.setDate(LocalDate.now());
        emailDao.addEmail(email);

        return "redirect:list"; //redirigim a la lista per a veure el reservation afegit, post/redirect/get
    }

    @RequestMapping(value="/update/{email}", method = RequestMethod.GET)
    public String editMunicipalManager(Model model, @PathVariable String email, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipalManager/update/" + email);
            return "redirect:/inicio/login";
        }
        if(session.getAttribute("section") != null)
            session.removeAttribute("section");
        model.addAttribute("municipalManager", municipalManagerDao.getMunicipalManager(email));
        return "municipalManager/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("municipalManager") MunicipalManager manager,
                                      BindingResult bindingResult) {
        MunicipalManagerValidator municipalManagerValidator = new MunicipalManagerValidator();
        municipalManagerValidator.validate(manager, bindingResult);

        if (bindingResult.hasErrors())
            return "municipalManager/update";
        municipalManagerDao.updateMunicipalManager(manager);
        return "redirect:/municipalManager/list";
    }

    @RequestMapping(value="/darDeBaja/{email}", method = RequestMethod.GET)
    public String darDeBajaMunicipalManager(Model model, @PathVariable String email,HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipalManager/darDeBaja/" + email);
            return "redirect:/inicio/login";
        }
        MunicipalManager municipalManager = municipalManagerDao.getMunicipalManager(email);
        municipalManager.setLeavingDate(LocalDate.now());
        municipalManagerDao.updateMunicipalManager(municipalManager);
        if(session.getAttribute("section") != null)
            session.removeAttribute("section");
        return "redirect:/municipalManager/list";
    }

    @RequestMapping(value="/delete/{email}")
    public String processDelete(Model model, @PathVariable String email, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipalManager/delete/" + email);
            return "redirect:/inicio/login";
        }
        municipalManagerDao.deleteMunicipalManager(email);
        sanaUserDao.deleteSanaUser(email);
        return "redirect:../list";
    }

    @RequestMapping(value="/get/{email}")
    public String getMunicipalManager(Model model, @PathVariable String email, HttpSession session){
        model.addAttribute("municipalManager", municipalManagerDao.getMunicipalManager(email));
        return "/municipalManager/get";
    }

    @RequestMapping("/perfil")
    public String perfilMunicipalManager(Model model, HttpSession session) {
        if (session.getAttribute("municipalManager") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipalManager/perfil");
            return "redirect:/inicio/login";
        }
        MunicipalManager municipalManager = (MunicipalManager) session.getAttribute("municipalManager");
        model.addAttribute("municipalManager", municipalManager);
        return "/municipalManager/perfil";
    }
}
