package es.uji.ei102720mgph.SANA.controller;



import es.uji.ei102720mgph.SANA.dao.TemporalServiceDao;
import es.uji.ei102720mgph.SANA.model.TemporalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/temporalService")
public class TemporalServiceController {

    private TemporalServiceDao temporalServiceDao;

    @Autowired
    public void setTemporalServiceDao(TemporalServiceDao temporalServiceDao) {
        this.temporalServiceDao=temporalServiceDao;
    }

    // Operació llistar
    @RequestMapping("/list")
    public String listTemporalServices(Model model) {
        model.addAttribute("temporalService", temporalServiceDao.getTemporalServices());
        return "temporalService/list";
    }

    // Operació crear
    @RequestMapping(value="/add")
    public String addTemporalService(Model model) {
        model.addAttribute("temporalService", new TemporalService());
        return "temporalService/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("temporalService") TemporalService temporalService,
                                   BindingResult bindingResult) {
        System.out.println("Añadir Temporal Service COntroller: "+temporalService.toString());
        if (bindingResult.hasErrors())
            return "temporalService/add"; //tornem al formulari per a que el corregisca
        temporalServiceDao.addTemporalService(temporalService); //usem el dao per a inserir el temporalService
        return "redirect:list"; //redirigim a la lista per a veure el temporalService afegit, post/redirect/get
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{service}/{naturalArea}", method = RequestMethod.GET)
    public String editTemporalService(Model model, @PathVariable String service, @PathVariable String naturalArea) {
        model.addAttribute("temporalService", temporalServiceDao.getTemporalService(service, naturalArea));
        return "temporalService/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("temporalService") TemporalService temporalService,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "temporalService/update";
        temporalServiceDao.updateTemporalService(temporalService);
        return "redirect:list";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{service}/{naturalArea}")
    public String processDelete(@PathVariable String service, @PathVariable String naturalArea) {
        temporalServiceDao.deleteTemporalService(service, naturalArea);
        return "redirect:../list";
    }
}
