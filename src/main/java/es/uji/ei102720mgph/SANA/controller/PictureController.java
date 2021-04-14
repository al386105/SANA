package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.PictureDao;
import es.uji.ei102720mgph.SANA.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/picture")
public class PictureController {
    private PictureDao pictureDao;

    @Autowired
    public void setPictureDao(PictureDao pictureDao){ this.pictureDao = pictureDao; }

    // Operació llistar
    @RequestMapping("/list")
    public String listPictures(Model model) {
        model.addAttribute("pictures", pictureDao.getPictures());
        return "picture/list";
    }

    //TODO Operació llistar pictures de un naturalArea


    // Operació crear
    @RequestMapping(value="/add")
    public String addPicture(Model model) {
        model.addAttribute("picture", new Picture());
        return "picture/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("picture") Picture picture,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "picture/add"; //tornem al formulari per a que el corregisca
        pictureDao.addPicture(picture); //usem el dao per a inserir la picture
        return "redirect:list"; //redirigim a la lista per a veure la picture afegit, post/redirect/get
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{picturePath:.+}", method = RequestMethod.GET)
    public String editPicture(Model model, @PathVariable String picturePath) {
        model.addAttribute("picture", pictureDao.getPicture(picturePath));
        return "picture/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("picture") Picture picture,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "picture/update";
        pictureDao.updatePicture(picture);
        return "redirect:list";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{picturePath:.+}")
    public String processDelete(@PathVariable String picturePath) {
        pictureDao.deletePicture(picturePath);
        return "redirect:../list";
    }
}
