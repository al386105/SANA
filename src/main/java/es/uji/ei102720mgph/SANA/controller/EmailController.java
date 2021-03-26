package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.EmailDao;
import es.uji.ei102720mgph.SANA.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/email")
public class EmailController {

    private EmailDao emailDao;

    @Autowired
    public void setEmailDao(EmailDao emailDao) {
        this.emailDao=emailDao;
    }

    // Operació llistar
    @RequestMapping("/list")
    public String listEmails(Model model) {
        model.addAttribute("emails", emailDao.getEmails());
        return "email/list";
    }

    // Operació crear
    @RequestMapping(value="/add")
    public String addEmail(Model model) {
        model.addAttribute("email", new Email());
        return "email/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("email") Email email,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "email/add"; //tornem al formulari per a que el corregisca
        emailDao.addEmail(email); //usem el dao per a inserir el email
        return "redirect:list"; //redirigim a la lista per a veure el email afegit, post/redirect/get
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editEmail(Model model, @PathVariable String id) {
        model.addAttribute("email", emailDao.getEmail(id));
        return "email/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("email") Email email,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "email/update";
        emailDao.updateEmail(email);
        return "redirect:list";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable String id) {
        emailDao.deleteEmail(id);
        return "redirect:../list";
    }
}
