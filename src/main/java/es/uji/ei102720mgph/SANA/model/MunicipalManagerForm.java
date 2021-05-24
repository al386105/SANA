package es.uji.ei102720mgph.SANA.model;

public class MunicipalManagerForm extends MunicipalManager {
    private String password2;

    public MunicipalManagerForm(){
        super();
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
