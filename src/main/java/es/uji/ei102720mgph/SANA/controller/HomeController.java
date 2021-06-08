package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.enums.TypeOfUser;
import es.uji.ei102720mgph.SANA.model.*;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDate;
import java.util.Formatter;
import java.util.Properties;

@Controller
@RequestMapping("/")
public class HomeController {

    private RegisteredCitizenDao registeredCitizenDao;
    private SanaUserDao sanaUserDao;
    private ControlStaffDao controlStaffDao;
    private AddressDao addressDao;
    private MunicipalManagerDao municipalManagerDao;
    private EmailDao emailDao;

    @Autowired
    public void setMunicipalManagerDao(MunicipalManagerDao municipalManagerDao){
        this.municipalManagerDao = municipalManagerDao;
    }

    @Autowired
    public void setEmailDao(EmailDao emailDao){
        this.emailDao = emailDao;
    }

    @Autowired
    public void setSanaUserDao(SanaUserDao sanaUserDao){
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
        if (session.getAttribute("environmentalManager") != null)
            return "environmentalManager/home";
        else if (session.getAttribute("municipalManager") != null)
            return "municipalManager/home";
        else if (session.getAttribute("registeredCitizen") != null)
            model.addAttribute("typeUser", "registeredCitizen");
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

    @RequestMapping(value="/inicio/register_form", method= RequestMethod.POST)
    public String registrationProcess(@ModelAttribute("registrationCitizen") RegistrationCitizen registrationCitizen,
                                      BindingResult bindingResult, HttpSession session, Model model){
        RegistrationValidator registrationValidator = new RegistrationValidator();
        registrationValidator.validate(registrationCitizen, bindingResult);

        if (bindingResult.hasErrors())
            return "/inicio/register_form"; //tornem al formulari d'inici de sessió

        SanaUser sanaUser = sanaUserDao.getSanaUser(registrationCitizen.getEmail());
        if (sanaUser == null){
            //Usuario no registrado antes

            if(registeredCitizenDao.getRegisteredCitizenNIE(registrationCitizen.getDni()) != null) {
                bindingResult.rejectValue("dni", "repetido", "DNI/NIE ya registrado");
                return "/inicio/register_form";
            }
            if(registeredCitizenDao.getRegisteredCitizenTelf(registrationCitizen.getTelefono()) != null) {
                bindingResult.rejectValue("telefono", "repetido", "Teléfono ya registrado");
                return "/inicio/register_form";
            }

            //Añadimos la dirección a las tablas
            Address address = new Address();
            address.setStreet(registrationCitizen.getStreet());
            address.setNumber(registrationCitizen.getNumber());
            address.setFloorDoor(registrationCitizen.getFloorDoor());
            address.setPostalCode(registrationCitizen.getPostalCode());
            address.setCity(registrationCitizen.getCity());
            address.setCountry(registrationCitizen.getCountry());
            String addressId = addressDao.addAddress(address);

            //Añadimos el ciudadano a RegisteredCitizen
            BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
            RegisteredCitizen registeredCitizen = new RegisteredCitizen();
            registeredCitizen.setAddressId(addressId);
            registeredCitizen.setName(registrationCitizen.getNombre());
            registeredCitizen.setPin(encryptor.encryptPassword(registrationCitizen.getPassword()));
            registeredCitizen.setSurname(registrationCitizen.getApellidos());
            registeredCitizen.setEmail(registrationCitizen.getEmail());
            registeredCitizen.setMobilePhoneNumber(registrationCitizen.getTelefono());
            registeredCitizen.setDateOfBirth(registrationCitizen.getDateOfBirth());
            registeredCitizen.setTypeOfUser(TypeOfUser.registeredCitizen);
            registeredCitizen.setIdNumber(registrationCitizen.getDni());
            Formatter fmt = new Formatter();

            int citizenCode = registeredCitizenDao.addRegisteredCitizen(registeredCitizen);
            String code = "ci" + fmt.format("%04d" , citizenCode);
            registeredCitizen.setCitizenCode(code);

            // Envia correo electrónico
            String destinatario = registeredCitizen.getEmail();
            String asunto = "Bienvenido a SANA";
            String cuerpo = "Registro completado con éxito en SANA, " + registeredCitizen.getName()+
                    ".\nSu código de usuario es: " + code +
                    ".\n\nUn cordial saludo del equipo de SANA.";
            Email emailObjeto = enviarMail(destinatario, asunto, cuerpo);
            emailDao.addEmail(emailObjeto);

            session.setAttribute("registeredCitizen", registeredCitizen);
            return "redirect:/inicio/registrado/welcome";

        }else {
            //Usuario ya registrado en el sistema
            bindingResult.rejectValue("email", "bademail", "Email ya registrado");
            return "/inicio/register_form";
        }
    }
    

    @RequestMapping(value="inicio/login", method=RequestMethod.POST)
    public String autenticationProcess(@ModelAttribute("userLogin") UserLogin userLogin, BindingResult bindingResult, HttpSession session){
        UserValidator userValidator = new UserValidator();
        userValidator.validate(userLogin, bindingResult);
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        if (bindingResult.hasErrors())
            return "inicio/login"; //tornem al formulari d'inici de sessió

        //Acceso del responsable
        try{
            String archivo = "responsableMedioambiental.txt";
            FileReader fileReader = new FileReader(archivo);
            BufferedReader b = new BufferedReader(fileReader);
            String r_email = b.readLine().trim();
            String r_pass = b.readLine().trim();
            b.close();

            if(userLogin.getUsername().equals(r_email) && encryptor.checkPassword(userLogin.getPassword(), r_pass)) {
                session.setAttribute("environmentalManager", "dentro");
                String nextUrl = (String) session.getAttribute("nextUrl");
                if (nextUrl != null) {
                    // Eliminar atribut de la sessio
                    session.removeAttribute("nextUrl");
                    return "redirect:" + nextUrl;
                }
                return "redirect:/environmentalManager/home";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // usuarios con ese email o citizen code
        SanaUser sanaUser = sanaUserDao.getSanaUser(userLogin.getUsername().trim());
        RegisteredCitizen registeredCitizen = registeredCitizenDao.getRegisteredCitizenCitizenCode(userLogin.getUsername());

        //El usuario esta registrado en el sistema
        if (sanaUser != null || registeredCitizen != null){
            // ACCESO GESTOR MUNICIPAL
            if (sanaUser != null && sanaUser.getTypeOfUser().equals(TypeOfUser.municipalManager)){
                MunicipalManager municipalManager = municipalManagerDao.getMunicipalManager(sanaUser.getEmail());
                // Si está dado de baja, no puede entrar
                if(municipalManager.getLeavingDate() != null) {
                    //El usuario no está registrado en el sistema
                    bindingResult.rejectValue("email", "dadoDeBaja", "Ha sido dado de baja como gestor");
                    return "inicio/login";
                }
                // Gestor municipal dado de alta
                if (encryptor.checkPassword(userLogin.getPassword(), municipalManager.getPassword())){
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

            // ACCESO PERSONAL DE CONTROL
            /*else if (sanaUser != null && sanaUser.getTypeOfUser().equals(TypeOfUser.controlStaff)){
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
            }*/

            // CIUDADANO AUTENTICADO CON SU CITIZEN CODE
            else if (sanaUser == null || sanaUser.getTypeOfUser().equals(TypeOfUser.registeredCitizen)) {
                try {
                    if (registeredCitizen == null)
                        registeredCitizen = registeredCitizenDao.getRegisteredCitizen(sanaUser.getEmail());

                    if (encryptor.checkPassword(userLogin.getPassword(), registeredCitizen.getPin())) {
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

            // CIUDADANO INTENTANDO ACCEDER CON SU CORREO
            else {
                bindingResult.rejectValue("username", "badUsername", "Inicie sesión con su código de usuario");
                return "inicio/login";
            }

        } else {
            //El usuario no está registrado en el sistema
            bindingResult.rejectValue("username", "badUsername", "Usuario no registrado en el sistema");
            return "inicio/login";
        }
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
        Email emailObjeto = enviarMail(destinatario1, asunto1, cuerpo1);
        // Anyadir a la tabla de email
        emailDao.addEmail(emailObjeto);
        emailObjeto = enviarMail(destinatario2, asunto2, cuerpo2);
        emailDao.addEmail(emailObjeto);

        return "redirect:/naturalArea/pagedlist"; //redirigim a la lista per a veure el email afegit, post/redirect/get
    }

    public static Email enviarMail(String destinatario, String asunto, String cuerpo) {
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
        Email email = new Email();

        try {
            message.setFrom(new InternetAddress(remitente));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));   //Se podrían añadir varios de la misma manera
            message.setSubject(asunto);
            message.setText(cuerpo);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, "barrachina");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            // crear email

            email.setSanaUser(destinatario);
            email.setSubject(asunto);
            email.setTextBody(cuerpo);
            email.setSender("sana.espais.naturals@gmail.com");
            email.setDate(LocalDate.now());
            return email;
        }
        catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
        }
        return email;
    }

}
