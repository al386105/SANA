package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.Email;
import es.uji.ei102720mgph.SANA.model.SanaUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Controller
@RequestMapping("/")
public class AuxiliarController {

    @RequestMapping("inicio")
    public String redirigirSana(Model model) {
        return "inicio/sana";
    }

    @RequestMapping("inicio/contactanos")
    public String redirigirContactanos(Model model) {
        model.addAttribute("email", new Email());
        return "inicio/contactanos";
    }

    @RequestMapping("inicio/login")
    public String redirigirLogin(Model model) {
        model.addAttribute("user", new SanaUser() {});
        return "inicio/login";
    }

    @RequestMapping("inicio/register_form")
    public String redirigirRegistro(Model model) {
        return "inicio/register_form";
    }
    /*
    @RequestMapping("inicio/login/autentication", method=RequestMethod.POST)
    UserValidator userValidator = new UserValidator();
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
        return "login";
    }
    // Comprova que el login siga correcte
    // intentant carregar les dades de l'usuari
    user = userDao.loadUserByUsername(user.getUsername(), user.getPassword());
        if (user == null) {
        bindingResult.rejectValue("password", "badpw", "Contrasenya incorrecta");
        return "login";
    }
    // Autenticats correctament.
    // Guardem les dades de l'usuari autenticat a la sessió
        session.setAttribute("user", user);
    String url = (String) session.getAttribute("nextUrl");
        session.removeAttribute("nextUrl");
        if (url != null)
            return "redirect:"+url;
    // Torna a la pàgina principal
        return "redirect:/";
     }
     */



    @RequestMapping(value="inicio/contactanos/enviarCorreo", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("email") Email email,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "inicio/contactanos"; //tornem al formulari per a que el corregisca

        String destinatario = email.getSanaUser();
        String asunto = email.getSubject();
        String cuerpo = email.getTextBody();

        // Envia correo electrónico
        enviarMail(destinatario, asunto, cuerpo);


        return "redirect:../../inicio"; //redirigim a la lista per a veure el email afegit, post/redirect/get
    }

    public void enviarMail(String destinatario, String asunto, String cuerpo) {
        String remitente = "sana.espais.naturals";  //Para la dirección nomcuenta@gmail.com

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", "barrachina");    //La clave de la cuenta
        props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
        props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
        props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(remitente));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));   //Se podrían añadir varios de la misma manera
            message.setSubject(asunto);
            message.setText(cuerpo);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, "barrachina");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
        }
    }

    @RequestMapping("section/managers")
    public String sectionManagers(Model model) {
        return "section/managers";
    }

    @RequestMapping("section/environmentalManager")
    public String sectionEnvironmentalmanager(Model model) {
        return "section/environmentalManager";
    }
}
