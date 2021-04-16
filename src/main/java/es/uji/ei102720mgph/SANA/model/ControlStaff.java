package es.uji.ei102720mgph.SANA.model;

public class ControlStaff extends SanaUser {
    private String userName;
    private String password; //hacer encrypted

    public ControlStaff() {
        super();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ControlStaff{" +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
