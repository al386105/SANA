package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.CommentDao;
import es.uji.ei102720mgph.SANA.model.Comment;
import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
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
import java.time.LocalDate;

@Controller
@RequestMapping("comment")
public class CommentController {

    private CommentDao commentDao;

    @Autowired
    public void setCommentDao(CommentDao commentDao) { this.commentDao = commentDao; }

    // Operació llistar
    @RequestMapping("/list")
    public String listComments(Model model) {
        model.addAttribute("comments", commentDao.getComments());
        return "comment/list";
    }

    // Operació crear
    @RequestMapping(value="/add/{naturalArea}")
    public String addComment(Model model, HttpSession session, @PathVariable String naturalArea) {
        RegisteredCitizen citizen = (RegisteredCitizen) session.getAttribute("registeredCitizen");
        if (citizen == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/comment/add/" + naturalArea);
            return "redirect:/inicio/login";
        }
        Comment comment = new Comment();
        comment.setNaturalArea(naturalArea);
        comment.setCitizenEmail(citizen.getEmail());
        session.setAttribute("section", "#comments");
        model.addAttribute("comment", comment);
        return "comment/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("comment") Comment comment, BindingResult bindingResult) {
        CommentValidator commentValidator = new CommentValidator();
        commentValidator.validate(comment, bindingResult);

        if (bindingResult.hasErrors())
            return "comment/add";
        System.out.println(comment.getCitizenEmail());
        commentDao.addComment(comment);
        return "redirect:/naturalArea/get/" + comment.getNaturalArea();
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{commentId}", method = RequestMethod.GET)
    public String editComment(Model model, @PathVariable String commentId, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/comment/update/" + commentId);
            return "redirect:/inicio/login";
        }
        model.addAttribute("comment", commentDao.getComment(commentId));
        return "comment/update";
    }

    // Resposta de modificació d'objectes
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("comment") Comment comment,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "comment/update";
        commentDao.updateComment(comment);
        return "redirect:list";
    }

    // Operació esborrar
    @RequestMapping(value="/delete/{commentId}")
    public String processDelete(Model model, @PathVariable String commentId, HttpSession session) {
        if (session.getAttribute("registeredCitizen") == null) {
            model.addAttribute("userLogin", new UserLogin() {});
            session.setAttribute("nextUrl", "/comment/update/" + commentId);
            return "redirect:/inicio/login";
        }
        commentDao.deleteComment(commentId);
        return "redirect:../list";
    }
}
