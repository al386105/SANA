package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.Comment;
import es.uji.ei102720mgph.SANA.model.Municipality;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Controller
public class CommentValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return Municipality.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Comment comment = (Comment)obj;

        // Cuerpo del comentario obligatorio
        if (comment.getCommentBody().trim().equals(""))
            errors.rejectValue("commentBody", "obligatorio", "Es obligatorio introducir el cuerpo del comentario");

        // Puntuación obligatoria
        if (comment.getScore() == null)
            errors.rejectValue("score", "obligatorio", "Es obligatorio seleccionar la puntuación del comentario");
    }
}