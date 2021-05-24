package es.uji.ei102720mgph.SANA.model;

public class RegisteredCitizen extends SanaUser {
    private String idNumber;
    private String mobilePhoneNumber;
    private  String username;
    private static int  citizenCode = 00001;
    private int pin;
    private String addressId;

    public RegisteredCitizen(){
        super();
    }

    public static void incrementaCitizenCode() {
        RegisteredCitizen.citizenCode++;
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

    public static int getCitizenCode() {
        return citizenCode;
    }

    public void setCitizenCode(int citizenCode) {
        RegisteredCitizen.citizenCode = citizenCode;
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
