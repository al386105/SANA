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
    /*@RequestMapping("/list")
    public String listEmails(Model model) {
        model.addAttribute("emails", emailDao.getEmails());
        return "email/list";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable String id) {
        emailDao.deleteEmail(id);
        return "redirect:../list";
    }*/
}
