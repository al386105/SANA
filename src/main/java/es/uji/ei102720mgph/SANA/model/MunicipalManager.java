package es.uji.ei102720mgph.SANA.model;

public class MunicipalManager extends SanaUser implements Comparable<MunicipalManager> {
    private String username;
    private String password;
    private String municipality;

    public MunicipalManager(){
        super();
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

    public int compareTo(MunicipalManager altre) {
        return this.getName().compareTo(altre.getName());
    }

    @Override
    public String toString() {
        return "MunicipalManager{" +
                ", usuario=" + username +
                ", password=" + password +
                ", municipality=" + municipality +
                '}';
    }

}
