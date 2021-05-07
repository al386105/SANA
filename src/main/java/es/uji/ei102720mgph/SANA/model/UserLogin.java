package es.uji.ei102720mgph.SANA.model;

public class UserLogin {

    String email;
    String password;

    public UserLogin(){
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
