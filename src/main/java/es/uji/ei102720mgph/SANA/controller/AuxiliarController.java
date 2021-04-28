package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.Email;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class AuxiliarController {

    @RequestMapping("inicio")
    public String redirigirSana(Model model) {
        model.addAttribute("email", new Email());
        return "inicio/sana";
    }

    @RequestMapping(value="/enviarCorreo", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("email") Email email,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "inicio/sana"; //tornem al formulari per a que el corregisca

        System.out.println("ID: " + email.getId());
        System.out.println("Sender: " + email.getSender());
        System.out.println("Date: " + email.getDate());
        System.out.println("SanaUser: " + email.getSanaUser());
        System.out.println("Subject: " + email.getSubject());
        System.out.println("Body: " + email.getTextBody());
        return "redirect:inicio/sana"; //redirigim a la lista per a veure el email afegit, post/redirect/get
    }
}
