package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.TimeSlotDao;
import es.uji.ei102720mgph.SANA.model.TimeSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/timeSlot")
public class TimeSlotController {

    private TimeSlotDao timeSlotDao;

    @Autowired
    public void setTimeSlotDao(TimeSlotDao tsd){
        this.timeSlotDao = tsd;
    }

    @RequestMapping("/list")
    public String listTimeSlot(Model model) {
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlots());
        return "timeSlot/list";
    }

    @RequestMapping(value="/add")
    public String addTimeSlot(Model model) {
        model.addAttribute("timeSlot", new TimeSlot());
        return "timeSlot/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("timeSlot") TimeSlot slot,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "timeSlot/add"; //tornem al formulari per a que el corregisca
        timeSlotDao.addTimeSlot(slot);
        return "redirect:list"; //redirigim a la lista, post/redirect/get
    }

    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editTimeSlot(Model model, @PathVariable String id) {
        model.addAttribute("timeSlot", timeSlotDao.getTimeSlot(id));
        return "timeSlot/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("timeSlot") TimeSlot slot,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "timeSlot/update";
        timeSlotDao.updateTimeSlot(slot);
        return "redirect:list";
    }

    @RequestMapping(value="/delete/{id}")
    public String processDelete(@PathVariable String id) {
        timeSlotDao.deleteTimeSlot(id);
        return "redirect:../list";
    }
}
