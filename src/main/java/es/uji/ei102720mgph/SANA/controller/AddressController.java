package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.AddressDao;
import es.uji.ei102720mgph.SANA.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/address")
public class AddressController {

    private AddressDao addressDao;

    @Autowired
    public void setAddressDao(AddressDao addressDao) {
        this.addressDao=addressDao;
    }

    // Operació llistar
    @RequestMapping("/list")
    public String listAddresss(Model model) {
        model.addAttribute("addresss", addressDao.getAddresss());
        return "address/list";
    }

    // Operació crear, creació d'objectes
    @RequestMapping(value="/add")
    public String addAddress(Model model) {
        model.addAttribute("address", new Address());
        return "address/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("address") Address address,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "address/add"; //tornem al formulari per a que el corregisca
        addressDao.addAddress(address); //usem el dao per a inserir el address
        return "redirect:list"; //redirigim a la lista per a veure el address afegit, post/redirect/get
    }

    // Operació actualitzar, modificació d'objectes
    @RequestMapping(value="/update/{nom}", method = RequestMethod.GET)
    public String editAddress(Model model, @PathVariable String nom) {
        model.addAttribute("address", addressDao.getAddress(nom));
        return "address/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("address") Address address,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "address/update";
        addressDao.updateAddress(address);
        return "redirect:list";
    }

    // Operació esborrar, mètode d'esborrat
    @RequestMapping(value="/delete/{nom}")
    public String processDelete(@PathVariable String nom) {
        addressDao.deleteAddress(nom);
        return "redirect:../list";
    }
}
