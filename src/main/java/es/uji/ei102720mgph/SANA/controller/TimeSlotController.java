package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.TimeSlotDao;
import es.uji.ei102720mgph.SANA.model.TimeSlot;
import es.uji.ei102720mgph.SANA.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/timeSlot")
public class TimeSlotController {
    private TimeSlotDao timeSlotDao;

    @Autowired
    public void setTimeSlotDao(TimeSlotDao timeSlotDao){
        this.timeSlotDao = timeSlotDao;
    }

    @RequestMapping(value="/add/{naturalArea}")
    public String addTimeSlot(Model model, @PathVariable String naturalArea, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/timeSlot/add/" + naturalArea);
            return "redirect:/inicio/login";
        }
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setNaturalArea(naturalArea);
        model.addAttribute("timeSlot", timeSlot);
        model.addAttribute("naturalArea", naturalArea);
        return "timeSlot/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("timeSlot") TimeSlot timeSlot,
                                   BindingResult bindingResult) {
        TimeSlotValidator timeSlotValidator = new TimeSlotValidator();
        timeSlotValidator.validate(timeSlot, bindingResult);

        if (bindingResult.hasErrors())
            return "timeSlot/add";
        timeSlotDao.addTimeSlot(timeSlot);
        String naturalAreaName = timeSlot.getNaturalArea();
        return "redirect:/timeSlot/getForManagersTimeSlots/" + naturalAreaName;
    }

    @RequestMapping(value="/update/{id}", method = RequestMethod.GET)
    public String editTimeSlot(Model model, @PathVariable String id, HttpSession session) {
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/timeSlot/update/" + id);
            return "redirect:/inicio/login";
        }
        model.addAttribute("timeSlot", timeSlotDao.getTimeSlot(id));
        return "timeSlot/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("timeSlot") TimeSlot timeSlot,
                                      BindingResult bindingResult) {
        TimeSlotValidator timeSlotValidator = new TimeSlotValidator();
        timeSlotValidator.validate(timeSlot, bindingResult);

        if (bindingResult.hasErrors())
            return "timeSlot/update";
        timeSlotDao.updateTimeSlot(timeSlot);
        String naturalAreaName = timeSlot.getNaturalArea();
        return "redirect:/timeSlot/getForManagersTimeSlots/" + naturalAreaName;
    }

    @RequestMapping(value="/getForManagersTimeSlots/{naturalArea}")
    public String getTimeSlotsForManagers(Model model, @PathVariable("naturalArea") String naturalArea, HttpSession session){
        if(session.getAttribute("municipalManager") ==  null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/timeSlot/getForManagersTimeSlots/" + naturalArea);
            return "redirect:/inicio/login";
        }
        model.addAttribute("naturalArea", naturalArea);
        model.addAttribute("timeSlots", timeSlotDao.getTimeSlotNaturalArea(naturalArea));
        return "/timeSlot/getForManagersTimeSlots";
    }
}
