package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.model.UserLogin;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return UserLogin.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {
        UserLogin user = (UserLogin) obj;

        if (user.getEmail().trim().equals(""))
            errors.rejectValue("email", "obligatorio", "Necesario introducir el email");

        if (user.getPassword().trim().equals(""))
            errors.rejectValue("password", "obligatorio", "Necesario introducir la contraseña");
    }
}
