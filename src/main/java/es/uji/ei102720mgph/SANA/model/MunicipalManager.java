package es.uji.ei102720mgph.SANA.model;

public class MunicipalManager {
    private String email;
    private String username;
    private String password;
    private String municipality;

    public MunicipalManager(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    //Si el password es readOnly, no se debería poder hacer un set ¿no?
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "MunicipalManager{" +
                "email=" + email +
                ", usuario=" + username +
                ", password=" + password +
                ", municipality=" + municipality +
                '}';
    }
}
