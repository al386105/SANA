package es.uji.ei102720mgph.SANA.model;

public class RegisteredCitizen extends SanaUser {
    private String idNumber;
    private String mobilePhoneNumber;
    private String username;
    private String citizenCode;
    private int pin;
    private String addressId;
    private static int contador = 1;

    public RegisteredCitizen(){
        super();
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String id) {
        this.idNumber = id;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getCitizenCode() {
        return citizenCode;
    }

    public void setCitizenCode(String citizenCode) {
        this.citizenCode = citizenCode;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public  String getUsername() {
        return username;
    }

    public  void setUsername(String username) {
        this.username = username;
    }

    public static int getContador() {
        return contador;
    }

    public static void setContador(int contador) {
        RegisteredCitizen.contador = contador;
    }

    public static void incrementaContador() {
        RegisteredCitizen.contador++;
    }

    @Override
    public String toString(){
        return "RegisteredCitizen{" +
                "idNumber='" + idNumber + "\'" +
                "mobilePhoneNumber='" + mobilePhoneNumber + "\'" +
                "citizenCode='" + citizenCode + "\'" +
                "pin='" + pin + "\'" +
                "addressId='" + addressId + "\'" +
                "}";
    }
}
