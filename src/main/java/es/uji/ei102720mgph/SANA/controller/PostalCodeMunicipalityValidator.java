package es.uji.ei102720mgph.SANA.controller;

import es.uji.ei102720mgph.SANA.dao.PostalCodeMunicipalityDao;
import es.uji.ei102720mgph.SANA.dao.ServiceDao;
import es.uji.ei102720mgph.SANA.model.PostalCodeMunicipality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PostalCodeMunicipalityValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return PostalCodeMunicipality.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        PostalCodeMunicipality postalCodeMunicipality = (PostalCodeMunicipality)obj;

        // Codigo postal obligatorio
        if (postalCodeMunicipality.getPostalCode().trim().equals(""))
            errors.rejectValue("postalCode", "obligatorio", "Es obligatorio introducir el código postal");

        // Codigo postal numérico
        if(!esNumerico(postalCodeMunicipality.getPostalCode()))
            errors.rejectValue("postalCode", "formato incorrecto", "El código postal debe ser numérico");

        // Codigo postal de 5 dígitos
        if(postalCodeMunicipality.getPostalCode().trim().length() != 5)
            errors.rejectValue("postalCode", "formato incorrecto", "El código postal debe tener 5 dígitos");
    }

    // devuelve true si la cadena es numérica
    public boolean esNumerico(String cadena) {
        for (int i = 0; i < cadena.length(); i++)
            if (!Character.isDigit(cadena.charAt(i))) return false;
        return true;
    }
}


