package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.enums.TypeOfUser;
import es.uji.ei102720mgph.SANA.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/")
public class HomeController {
    private RegisteredCitizenDao registeredCitizenDao;
    private SanaUserDao sanaUserDao;
    private ControlStaffDao controlStaffDao;
    private AddressDao addressDao;
    private MunicipalManagerDao municipalManagerDao;


    @Autowired
    public void setMunicipalManagerDao(MunicipalManagerDao municipalManagerDao){
        this.municipalManagerDao = municipalManagerDao;
    }

    @Autowired
    public void setSanaUsertDao(SanaUserDao sanaUserDao){
        this.sanaUserDao = sanaUserDao;
    }

    @Autowired
    public void setControlStaffDao(ControlStaffDao controlStaffDao){
        this.controlStaffDao = controlStaffDao;
    }

    @Autowired
    public void setAddressDao(AddressDao addressDao){
        this.addressDao = addressDao;
    }


    @Autowired
    public void setRegisteredCitizenDao(RegisteredCitizenDao registeredCitizenDao){
        this.registeredCitizenDao = registeredCitizenDao;
    }

    @RequestMapping("/")
    public String home(Model model, HttpSession session) {
        if(session.getAttribute("registeredCitizen") != null)
            return "inicioRegistrado/home";
        else if (session.getAttribute("environmentalManager") != null)
            return "environmentalManager/home";
        else if (session.getAttribute("municipalManager") != null)
            return "municipalManager/home";
        return "inicio/home";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/inicio/login";
    }

    @RequestMapping("inicio/contactanos")
    public String redirigirContactanos(Model model, HttpSession session) {
        model.addAttribute("email", new Email());
        return "inicio/contactanos";
    }

    @RequestMapping("inicio/login")
    public String redirigirLogin(Model model, HttpSession session) {
        model.addAttribute("userLogin", new UserLogin() {});
        return "inicio/login";
    }

    @RequestMapping("inicio/register_form")
    public String redirigirRegistro(Model model) {
        model.addAttribute("registrationCitizen", new RegistrationCitizen());
        return "inicio/register_form";
    }

    @RequestMapping("inicio/register_form/registration")
    public String registrationProcess(@ModelAttribute("registrationCitizen") RegistrationCitizen registrationCitizen,BindingResult bindingResult, HttpSession session ){

        if (bindingResult.hasErrors())
            return "inicio/login"; //tornem al formulari d'inici de sessió

        SanaUser sanaUser = sanaUserDao.getSanaUser(registrationCitizen.getEmail());
        if (sanaUser == null){
            //Usuario no registrado antes

            //Añadimos la dirección a las tablas
            Address address = new Address();
            address.setStreet(registrationCitizen.getStreet());
            address.setNumber( registrationCitizen.getNumber());
            address.setFloorDoor(registrationCitizen.getFloorDoor());
            address.setPostalCode(registrationCitizen.getPostalCode());
            address.setCity(registrationCitizen.getCity());
            address.setCountry(registrationCitizen.getCountry());
            addressDao.addAddress(address);

            //Añadimos el ciudadano a RegisteredCitizen
            Formatter fmt = new Formatter();
            RegisteredCitizen registeredCitizen =  new RegisteredCitizen();
            registeredCitizen.setAddressId(addressDao.getAddress("ad" + fmt.format("%07d", Address.getContador()-1)).getId());
            registeredCitizen.setName(registrationCitizen.getNombre());
            registeredCitizen.setPin(Integer.parseInt(registrationCitizen.getPassword()));
            registeredCitizen.setSurname(registrationCitizen.getApellidos());
            registeredCitizen.setEmail(registrationCitizen.getEmail());
            registeredCitizen.setMobilePhoneNumber(registrationCitizen.getTelefono());
            registeredCitizen.setDateOfBirth(registrationCitizen.getDateOfBirth());
            registeredCitizen.setTypeOfUser(TypeOfUser.registeredCitizen);
            registeredCitizen.setIdNumber(registrationCitizen.getDni());
            registeredCitizen.setCitizenCode(registrationCitizen.getCitizenCode());
            registeredCitizenDao.addRegisteredCitizen(registeredCitizen);

            // TODO ENVIAR MAIL!!!!

            return "redirect:/inicio/login";
        }else {
            //Usuario ya registrado en el sistema
            bindingResult.rejectValue("email", "bademail", "Usuario ya registrado");
            return "redirect:/inicio/login";
        }
    }

    @RequestMapping(value="inicio/login", method=RequestMethod.POST)
    public String autenticationProcess(@ModelAttribute("userLogin") UserLogin userLogin, BindingResult bindingResult, HttpSession session){
        UserValidator userValidator = new UserValidator();
        userValidator.validate(userLogin, bindingResult);

        if (bindingResult.hasErrors())
            return "inicio/login"; //tornem al formulari d'inici de sessió


        //!!!!!!!!!
        // TODO acceso responsable fatal
        if(userLogin.getEmail().equals("responsable@gmail.com") && userLogin.getPassword().equals("responsable")) {
            session.setAttribute("environmentalManager", "dentro");
            String nextUrl = (String) session.getAttribute("nextUrl");
            if (nextUrl != null) {
                // Eliminar atribut de la sessio
                session.removeAttribute("nextUrl");
                return "redirect:" + nextUrl;
            }
            return "redirect:/environmentalManager/home";
        }
        //!!!!!!!!!


        SanaUser sanaUser = sanaUserDao.getSanaUser(userLogin.getEmail().trim());
        if (sanaUser != null){
            //El usuario esta registrado en el sistema

            if (sanaUser.getTypeOfUser().equals(TypeOfUser.municipalManager)){
                MunicipalManager municipalManager = municipalManagerDao.getMunicipalManager(sanaUser.getEmail());

                // Si está dado de baja, no puede entrar
                if(municipalManager.getLeavingDate() != null) {
                    //El usuario no está registrado en el sistema
                    bindingResult.rejectValue("email", "dadoDeBaja", "Ha sido dado de baja como gestor");
                    return "inicio/login";
                }

                // Gestor municipal dado de alta
                if (municipalManager.getPassword().equals(userLogin.getPassword())){
                    //Contraseña correcta
                    session.setAttribute("municipalManager", municipalManager);
                    // Comprova si l'usuari volia accedir a una altra pagina
                    String nextUrl = (String) session.getAttribute("nextUrl");
                    if (nextUrl != null) {
                        // Eliminar atribut de la sessio
                        session.removeAttribute("nextUrl");
                        return "redirect:" + nextUrl;
                    }
                    return "redirect:/municipalManager/home";
                }else{
                    //Contraseña Incorrecta
                    bindingResult.rejectValue("password", "badpw", "Contraseña incorrecta");
                    return "inicio/login";
                }
            }

            else if (sanaUser.getTypeOfUser().equals(TypeOfUser.controlStaff)){
                ControlStaff controlStaff = controlStaffDao.getControlStaf(sanaUser.getEmail());
                if (controlStaff.getPassword().equals(userLogin.getPassword())){
                    //Contraseña correcta
                    session.setAttribute("controlStaff", controlStaff);
                    return "redirect:/naturalArea/pagedlist";
                }else{
                    //Contraseña Incorrecta
                    bindingResult.rejectValue("password", "badpw", "Contraseña incorrecta");
                    return "inicio/login";
                }
            }

            else if (sanaUser.getTypeOfUser().equals(TypeOfUser.registeredCitizen)){
                RegisteredCitizen registeredCitizen = registeredCitizenDao.getRegisteredCitizen(sanaUser.getEmail());
                try {
                    if (registeredCitizen.getPin() == Integer.parseInt(userLogin.getPassword())) {
                        //Contraseña Correcta
                        session.setAttribute("registeredCitizen", registeredCitizen);
                        // Comprova si l'usuari volia accedir a una altra pagina
                        String nextUrl = (String) session.getAttribute("nextUrl");
                        if (nextUrl != null) {
                            // Eliminar atribut de la sessio
                            session.removeAttribute("nextUrl");
                            return "redirect:" + nextUrl;
                        }
                        return "redirect:/naturalArea/pagedlist";
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

        } else {
            //El usuario no está registrado en el sistema
            bindingResult.rejectValue("email", "badEmail", "Email no registrado en el sistema");
            return "inicio/login";
        }
        return "redirect:/naturalArea/pagedlist"; //Redirigimos a la página de inicio con la sesión iniciada
    }

    @RequestMapping(value="inicio/contactanos/enviarCorreo", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("email") Email email,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "inicio/contactanos"; //tornem al formulari per a que el corregisca

        String destinatario1 = email.getSanaUser();
        String asunto1 = "Su petición ha sido recibida con éxito";
        String cuerpo1 = "Gracias por contactar con nosotros.\n\n" +
                "Uno de nuestros responsables internos ha sido notificado de la situación," +
                " recibirás noticias sobre el incidente en breve.\n\n" +
                "Un cordial saludo del equipo de SANA.";
        String destinatario2 = "sana.espais.naturals@gmail.com";
        String asunto2 = "Nuevo mensaje recibido";
        String cuerpo2 = "Un usuario con correo: '" + email.getSanaUser() + "' ha enviado un correo.\n" +
                "Asunto: " + email.getSubject() + "\n" +
                "Cuerpo: " + email.getTextBody();

        // Envia correo electrónico
        enviarMail(destinatario1, asunto1, cuerpo1);
        enviarMail(destinatario2, asunto2, cuerpo2);

        return "redirect:/naturalArea/pagedlist"; //redirigim a la lista per a veure el email afegit, post/redirect/get
    }

    public static void enviarMail(String destinatario, String asunto, String cuerpo) {
        String remitente = "sana.espais.naturals";  //Para la dirección nomcuenta@gmail.com

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", System.getenv("PASS"));     //La clave de la cuenta
        props.put("mail.smtp.auth", "true");            //Usar autenticación mediante usuario y clave
        props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
        props.put("mail.smtp.port", "587");             //El puerto SMTP seguro de Google

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

}
