package es.uji.ei102720mgph.SANA.model;

public class UserLogin {

    String username;
    String password;

    public UserLogin(){
        super();
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

    public void setPassword(String password) {
        this.password = password;
    }
}
