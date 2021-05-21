package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.*;
import es.uji.ei102720mgph.SANA.enums.TypeOfUser;
import es.uji.ei102720mgph.SANA.model.*;
import es.uji.ei102720mgph.SANA.model.Address;
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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Formatter;
import java.util.List;
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
            errors.rejectValue("email", "obligatorio", "Necesario introducir el email");

        if (user.getPassword().trim().equals(""))
            errors.rejectValue("password", "obligatorio", "Necesario introducir la contraseña");
    }
}


@Controller
@RequestMapping("/")
public class AuxiliarController {
<<<<<<< HEAD
    private SanaUserDao sanaUserDao;
    private RegisteredCitizenDao registeredCitizenDao;
    private MunicipalManagerDao municipalManagerDao;
    private ControlStaffDao controlStaffDao;
    private AddressDao addressDao;

    @Autowired
    public void setControlStaffDao(ControlStaffDao controlStaffDao){
        this.controlStaffDao = controlStaffDao;
    }
=======
    @Value("${upload.file.directory}")
    private String uploadDirectory;
    private ReservaDatosDao reservaDatosDao;
    private ReservationDao reservationDao;
>>>>>>> 0ecf6c8f201d42770fba5785b26cc3e539bbbc7c



    @Autowired
<<<<<<< HEAD
    public void setMunicipalManagerDao(MunicipalManagerDao municipalManagerDao){
        this.municipalManagerDao = municipalManagerDao;
    }


    public void setReservationDao(ReservationDao reservationDao){
        this.reservationDao = reservationDao;
    }


    @Autowired
    public void setReservaDatosDao(ReservaDatosDao reservaDatosDao){
        this.reservaDatosDao = reservaDatosDao;
    }




}
