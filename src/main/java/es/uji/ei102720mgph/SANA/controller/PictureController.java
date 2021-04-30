package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.NaturalAreaDao;
import es.uji.ei102720mgph.SANA.dao.PictureDao;
import es.uji.ei102720mgph.SANA.model.NaturalArea;
import es.uji.ei102720mgph.SANA.model.Picture;
import es.uji.ei102720mgph.SANA.model.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Formatter;

@Controller
@RequestMapping("/picture")
public class PictureController {
    @Value("${upload.file.directory}")
    private String uploadDirectory;

    private PictureDao pictureDao;
    private NaturalAreaDao naturalAreaDao;

    @Autowired
    public void setPictureDao(PictureDao pictureDao){ this.pictureDao = pictureDao; }

    @Autowired
    public void setNaturalAreaDao(NaturalAreaDao naturalAreaDao){ this.naturalAreaDao = naturalAreaDao; }

    // Operació llistar
    @RequestMapping("/list")
    public String listPictures(Model model) {
        model.addAttribute("pictures", pictureDao.getPictures());
        return "picture/list";
    }

    // Operació crear
    @RequestMapping(value="/add/{naturalArea}")
    public String addPicture(Model model, @PathVariable String naturalArea) {
        Picture picture = new Picture();
        picture.setNaturalArea(naturalArea);
        model.addAttribute("picture", picture);
        return "picture/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add/{naturalArea}", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("picture") Picture picture, @RequestParam("file") MultipartFile file,
                                   @PathVariable String naturalArea,
                                   RedirectAttributes redirectAttributes, BindingResult bindingResult) {
        if (file.isEmpty()) {
            // Enviar mensaje de error porque no hay fichero seleccionado
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/uploadStatus";
        }
        try {
            if (bindingResult.hasErrors())
                return "picture/add";
            // Obtener el fichero y guardarlo
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDirectory + "naturalAreas/" + file.getOriginalFilename());
            Files.write(path, bytes);
            System.out.println(file.getOriginalFilename());
            picture.setPicturePath("/assets/img/naturalAreas/" + file.getOriginalFilename());
            picture.setNaturalArea(naturalArea);
            pictureDao.addPicture(picture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/naturalArea/getManagers/" + naturalArea;
    }

    // Operació esborrar
    @RequestMapping(value="/delete/assets/img/naturalAreas/{pictureName}")
    public String processDelete(@PathVariable String pictureName) {
        System.out.println(pictureName);
        Picture picture = pictureDao.getPicture("/assets/img/naturalAreas/" + pictureName);
        String naturalAreaName = picture.getNaturalArea();
        pictureDao.deletePicture("/assets/img/naturalAreas/" + pictureName);
        return "redirect:/naturalArea/getManagers/" + naturalAreaName;
    }
}
