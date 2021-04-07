package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.CommentDao;
import es.uji.ei102720mgph.SANA.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("comment")
public class CommentController {

    private CommentDao commentDao;

    @Autowired
    public void setCommentDao(CommentDao commentDao) { this.commentDao = commentDao; }

    // Operació llistar
    @RequestMapping("/list")
    public String listReservations(Model model) {
        model.addAttribute("comments", commentDao.getComments());
        return "comment/list";
    }

    // Operació crear
    @RequestMapping(value="/add")
    public String addComment(Model model) {
        model.addAttribute("comment", new Comment());
        return "comment/add";
    }

    // Gestió de la resposta del formulari de creació d'objectes
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("comment") Comment comment,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "comment/add"; //tornem al formulari per a que el corregisca
        commentDao.addComment(comment);
        return "redirect:list"; //redirigim a la lista per a veure el reservation afegit, post/redirect/get
    }

    // Operació actualitzar
    @RequestMapping(value="/update/{commentId}", method = RequestMethod.GET)
    public String editComment(Model model, @PathVariable String commentId) {
        model.addAttribute("comment", commentDao.getComment(commentId));
        return "reservation/update";
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
    public String processDelete(@PathVariable String commentId) {
        commentDao.deleteComment(commentId);
        return "redirect:../list";
    }
}
