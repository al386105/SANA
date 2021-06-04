package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.NaturalAreaDao;
import es.uji.ei102720mgph.SANA.dao.PictureDao;
import es.uji.ei102720mgph.SANA.model.Picture;
import es.uji.ei102720mgph.SANA.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/picture")
public class PictureController {
    @Value("${upload.file.directory}")
    private String uploadDirectory;

    private PictureDao pictureDao;

    @Autowired
    public void setPictureDao(PictureDao pictureDao){ this.pictureDao = pictureDao; }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add/{naturalArea}", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("picture") Picture picture, @RequestParam("file") MultipartFile file,
                                   @PathVariable String naturalArea, HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            // Enviar mensaje de error porque no hay fichero seleccionado
            redirectAttributes.addFlashAttribute("message", "Selecciona un fichero para subir");
            return "redirect:/uploadStatus";
        }
        try {
            // Obtener el fichero y guardarlo
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDirectory + "naturalAreas/" + file.getOriginalFilename());
            Files.write(path, bytes);
            picture.setPicturePath("/assets/img/naturalAreas/" + file.getOriginalFilename());
            picture.setNaturalArea(naturalArea);
            pictureDao.addPicture(picture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/naturalArea/getForManagers/" + naturalArea;
    }

    // Operació esborrar
    @RequestMapping(value="/delete/assets/img/naturalAreas/{pictureName}")
    public String processDelete(Model model, @PathVariable String pictureName, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/picture/delete/assets/img/naturalAreas/" + pictureName);
            return "redirect:/inicio/login";
        }
        Picture picture = pictureDao.getPicture("/assets/img/naturalAreas/" + pictureName);
        String naturalAreaName = picture.getNaturalArea();
        pictureDao.deletePicture("/assets/img/naturalAreas/" + pictureName);
        return "redirect:/naturalArea/getForManagers/" + naturalAreaName;
    }
}
