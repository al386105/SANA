package es.uji.ei102720mgph.SANA.model;


public class RegisteredCitizen {

    private String idNumber;
    private String email;
    private String mobilePhoneNumber;
    private String citizenCode;
    private int pin;
    private String addressId;

    public RegisteredCitizen(){
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String id) {
        this.idNumber = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


    @Override
    public String toString(){
        return "RegisteredCitizen{" +
                "id='" + idNumber + "\'" +
                "email'=" + email + "\'" +
                "mobilePhoneNumber='" + mobilePhoneNumber + "\'" +
                "citizenCode='" + citizenCode + "\'" +
                "pin='" + pin + "\'" +
                "ideAddress='" + addressId + "\'" +
                "}";
    }

}
