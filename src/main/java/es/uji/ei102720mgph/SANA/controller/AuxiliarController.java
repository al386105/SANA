package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.enums.TypeOfUser;
import es.uji.ei102720mgph.SANA.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.util.Properties;






class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return UserLogin.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {

        UserLogin user = (UserLogin) obj;

        if (user.getEmail().trim().equals(""))
            errors.rejectValue("email", "obligatorio", "Necesario Introducir Email");

        if (user.getPassword().trim().equals(""))
            errors.rejectValue("password", "obligatorio", "Necesario Introducir la contraseña");
    }
}


@Controller
@RequestMapping("/")
public class AuxiliarController {


    private SanaUserDao sanaUserDao;
    private RegisteredCitizenDao registeredCitizenDao;
    private MunicipalManagerDao municipalManagerDao;
    private ControlStaffDao controlStaffDao;

    @Autowired
    public void setControlStaffDao(ControlStaffDao controlStaffDao){
        this.controlStaffDao = controlStaffDao;
    }

    @Autowired
    public void setRegisteredCitizenDao(RegisteredCitizenDao registeredCitizenDao){
        this.registeredCitizenDao = registeredCitizenDao;
    }

    @Autowired
    public void setSanaUsertDao(SanaUserDao sanaUserDao){
        this.sanaUserDao = sanaUserDao;
    }

    @Autowired
    public void setMunicipalManagerDao(MunicipalManagerDao municipalManagerDao){
        this.municipalManagerDao = municipalManagerDao;
    }


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
        model.addAttribute("userLogin", new UserLogin() {});
        return "inicio/login";
    }

    @RequestMapping("inicio/register_form")
    public String redirigirRegistro(Model model) {
        return "inicio/register_form";
    }

    @RequestMapping(value="inicio/login/autentication", method=RequestMethod.POST)
    public String autenticationProcess(@ModelAttribute("userLogin") UserLogin userLogin, BindingResult bindingResult, HttpSession session){
        UserValidator userValidator = new UserValidator();
        userValidator.validate(userLogin, bindingResult);

        if (bindingResult.hasErrors())
            return "inicio/login"; //tornem al formulari d'inici de sessió

        SanaUser sanaUser = sanaUserDao.getSanaUser(userLogin.getEmail().trim());

        if (sanaUser != null){
            //El usuario esta registrado en el sistema

            if (sanaUser.getTypeOfUser().equals(TypeOfUser.municipalManager)){
                MunicipalManager municipalManager = municipalManagerDao.getMunicipalManager(sanaUser.getEmail());
                if (municipalManager.getPassword().equals(userLogin.getPassword())){
                    //Contraseña correcta
                    session.setAttribute("municipalManager", municipalManager);
                    return "section/managers";
                }else{
                    //Contraseña Incorrecta
                    bindingResult.rejectValue("password", "badpw", "Contraseña incorrecta");
                    return "inicio/login";
                }

            }
            if (sanaUser.getTypeOfUser().equals(TypeOfUser.controlStaff)){
                ControlStaff controlStaff = controlStaffDao.getControlStaf(sanaUser.getEmail());
                if (controlStaff.getPassword().equals(userLogin.getPassword())){
                    //Contraseña correcta
                    session.setAttribute("controlStaff", controlStaff);
                    return "inicio/sana";
                }else{
                    //Contraseña Incorrecta
                    bindingResult.rejectValue("password", "badpw", "Contraseña incorrecta");
                    return "inicio/login";
                }
            }

            if (sanaUser.getTypeOfUser().equals(TypeOfUser.registeredCitizen)){
                RegisteredCitizen registeredCitizen = registeredCitizenDao.getRegisteredCitizen(sanaUser.getEmail());
                try {

                    if (registeredCitizen.getPin() == Integer.parseInt(userLogin.getPassword())) {
                        //Contraseña Correcta
                        session.setAttribute("registeredCitizen", registeredCitizen);
                        return "inicio/sana";

                    } else {
                        //Contraseña Incorrecta
                        bindingResult.rejectValue("password", "badpw", "Contraseña incorrecta");
                        return "inicio/login";
                    }
                }catch (NumberFormatException n){
                    //Contraseña Incorrecta
                    bindingResult.rejectValue("password", "badpw", "Contraseña incorrecta");
                    return "inicio/login";
                }
            }

        }else{
            //El usuario no está registrado en el sistema
            bindingResult.rejectValue("email", "badEmail", "Email no registrado en el sistema");
            return "inicio/login";
        }

        return "inicio/sana"; //Redirigimos a la página de inicio con la sesión iniciada
    }



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
