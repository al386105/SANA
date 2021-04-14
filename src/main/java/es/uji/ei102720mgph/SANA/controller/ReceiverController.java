package es.uji.ei102720mgph.SANA.controller;

//import es.uji.ei102720mgph.SANA.dao.ReceiverDao;
//import es.uji.ei102720mgph.SANA.model.Receiver;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;

//@Controller
//@RequestMapping("/receiver")
//public class ReceiverController {
//
//    private ReceiverDao receiverDao;
//
//    @Autowired
//    public void setReceiverDao(ReceiverDao receiverDao) {
//        this.receiverDao=receiverDao;
//    }
//
//    // Operació llistar
//    @RequestMapping("/list")
//    public String listReceivers(Model model) {
//        model.addAttribute("receivers", receiverDao.getReceivers());
//        return "receiver/list";
//    }
//
//    // Operació crear
//    @RequestMapping(value="/add")
//    public String addReceiver(Model model) {
//        model.addAttribute("receiver", new Receiver());
//        return "receiver/add";
//    }
//
//    // Gestió de la resposta del formulari de creació d'objectes
//    @RequestMapping(value="/add", method=RequestMethod.POST)
//    public String processAddSubmit(@ModelAttribute("receiver") Receiver receiver,
//                                   BindingResult bindingResult) {
//        if (bindingResult.hasErrors())
//            return "receiver/add"; //tornem al formulari per a que el corregisca
//        receiverDao.addReceiver(receiver); //usem el dao per a inserir el receiver
//        return "redirect:list"; //redirigim a la lista per a veure el receiver afegit, post/redirect/get
//    }
//
//    // Operació actualitzar
//    @RequestMapping(value="/update/{email}", method = RequestMethod.GET)
//    public String editReceiver(Model model, @PathVariable String email) {
//        model.addAttribute("receiver", receiverDao.getReceiver(email));
//        return "receiver/update";
//    }
//
//    // Resposta de modificació d'objectes
//    @RequestMapping(value="/update", method = RequestMethod.POST)
//    public String processUpdateSubmit(@ModelAttribute("receiver") Receiver receiver,
//                                      BindingResult bindingResult) {
//        if (bindingResult.hasErrors())
//            return "receiver/update";
//        receiverDao.updateReceiver(receiver);
//        return "redirect:list";
//    }
//
//    // Operació esborrar
//    @RequestMapping(value="/delete/{email}")
//    public String processDelete(@PathVariable String email) {
//        receiverDao.deleteReceiver(email);
//        return "redirect:../list";
//    }
//}
