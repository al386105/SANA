package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.EmailDao;
import es.uji.ei102720mgph.SANA.dao.MunicipalManagerDao;
import es.uji.ei102720mgph.SANA.dao.MunicipalityDao;
import es.uji.ei102720mgph.SANA.dao.SanaUserDao;
import es.uji.ei102720mgph.SANA.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("home")
    public String sectionManagers(Model model, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            return "/inicio/login";
        }
        return "municipalManager/home";
    }

    @RequestMapping("/list")
    public String listMunicipalManager(Model model, HttpSession session, @RequestParam(value="patron",required=false) String patron) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipalManager/list");
            return "redirect:/inicio/login";
        }
        // Aplicar filtro
        List<MunicipalManager> managers;
        if (patron != null)
            managers = municipalManagerDao.getMunicipalManagersSearch(patron);
        else
            managers = municipalManagerDao.getMunicipalManagers();

        model.addAttribute("municipalManagers", managers);
        quitarAtributoSeccion(session);
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
        model.addAttribute("municipalManager", new MunicipalManagerForm());
        return "municipalManager/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(Model model, @ModelAttribute("municipalManager") MunicipalManagerForm managerForm,
                                   BindingResult bindingResult) {
        MunicipalManagerValidator municipalManagerValidator = new MunicipalManagerValidator();
        municipalManagerValidator.validate(managerForm, bindingResult);

        // si ha saltado la excepción, quitar su atributo
        if(model.getAttribute("emailRepetido") != null)
            model.addAttribute("emailRepetido", null);
        if(model.getAttribute("usernameRepetido") != null)
            model.addAttribute("usernameRepetido", null);
        if(model.getAttribute("selector") != null)
            model.addAttribute("selector", null);

        if (bindingResult.hasErrors())
            return "municipalManager/add"; //tornem al formulari per a que el corregisca
        try {
            municipalManagerDao.addMunicipalManager(pasoAMunicipalManager(managerForm));

            // Enviar mail al nuevo municipal manager
            String destinatario = managerForm.getEmail();
            String asunto = "Dado de alta";
            String cuerpo = "¡Ha sido dado de alta en SANA como gestor municipal en " + managerForm.getMunicipality() + "! \n" +
                    "Su usuario es " + managerForm.getUsername() + " y su contraseña es " + managerForm.getPassword() + ". \n\n" +
                    "Un cordial saludo del equipo de SANA.";
            Email email = HomeController.enviarMail(destinatario, asunto, cuerpo);
            emailDao.addEmail(email);

        } catch (DataIntegrityViolationException e) {
            // hay alguna clave primaria o alternativa repetida, ver cuál es
            if(sanaUserDao.getSanaUser(managerForm.getEmail()) != null)
                model.addAttribute("emailRepetido", "repetido");
            // se inserta en user para pasar al resto de comprobaciones, debo eliminarlo, sino da error de email repetido
            municipalManagerDao.deleteMunicipalManager(managerForm.getEmail());
            if(municipalManagerDao.getMunicipalManagerUsername(managerForm.getUsername()) != null)
                model.addAttribute("usernameRepetido", "repetido");
            // selector no seleccionado
            if(managerForm.getMunicipality().equals("No seleccionado"))
                model.addAttribute("selector", "noSeleccionado");
            return "municipalManager/add";
        }
        return "redirect:list";
    }

    private MunicipalManager pasoAMunicipalManager(MunicipalManagerForm managerForm) {
        MunicipalManager manager = new MunicipalManager();
        manager.setMunicipality(managerForm.getMunicipality());
        manager.setPassword(managerForm.getPassword());
        manager.setUsername(managerForm.getUsername());
        manager.setDateOfBirth(managerForm.getDateOfBirth());
        manager.setEmail(managerForm.getEmail());
        manager.setName(managerForm.getName());
        manager.setSurname(managerForm.getSurname());
        manager.setRegistrationDate(managerForm.getRegistrationDate());
        manager.setTypeOfUser(managerForm.getTypeOfUser());
        if(managerForm.getLeavingDate() != null)
            manager.setLeavingDate(managerForm.getLeavingDate());
        return manager;
    }

    @RequestMapping(value="/update/{email}", method = RequestMethod.GET)
    public String editMunicipalManager(Model model, @PathVariable String email, HttpSession session) {
        if(session.getAttribute("environmentalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/municipalManager/update/" + email);
            return "redirect:/inicio/login";
        }
        quitarAtributoSeccion(session);
        MunicipalManager manager = municipalManagerDao.getMunicipalManager(email);

        // Pasar al objeto de MunicipalManagerForm para el formulario
        MunicipalManagerForm municipalManagerForm = new MunicipalManagerForm();
        municipalManagerForm.setName(manager.getName());
        municipalManagerForm.setSurname(manager.getSurname());
        municipalManagerForm.setPassword(manager.getPassword());
        municipalManagerForm.setDateOfBirth(manager.getDateOfBirth());
        municipalManagerForm.setEmail(manager.getEmail());
        municipalManagerForm.setMunicipality(manager.getMunicipality());
        municipalManagerForm.setRegistrationDate(manager.getRegistrationDate());
        municipalManagerForm.setTypeOfUser(manager.getTypeOfUser());
        municipalManagerForm.setUsername(manager.getUsername());
        if(manager.getLeavingDate() != null)
            municipalManagerForm.setLeavingDate(manager.getLeavingDate());
        model.addAttribute("municipalManager", municipalManagerForm);
        return "municipalManager/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(Model model, @ModelAttribute("municipalManager") MunicipalManagerForm managerForm,
                                      BindingResult bindingResult) {
        // ya que en el validador hay password2 y es vacía al actualizar
        managerForm.setPassword2(managerForm.getPassword());
        MunicipalManagerValidator municipalManagerValidator = new MunicipalManagerValidator();
        municipalManagerValidator.validate(managerForm, bindingResult);
        if (bindingResult.hasErrors())
            return "municipalManager/update";
        municipalManagerDao.updateMunicipalManager(pasoAMunicipalManager(managerForm));
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
        return "redirect:/municipalManager/list";
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

    private void quitarAtributoSeccion(HttpSession session) {
        if(session.getAttribute("section") != null)
            session.removeAttribute("section");
    }
}
