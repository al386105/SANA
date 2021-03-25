package es.uji.ei102720mgph.SANA.model;

import java.util.Date;

public class RegisteredCitizen {

    private String name;
    private String surname;
    private String id;
    private String email;
    private String mobilePhoneNumber;
    private String address;
    private Date dateOfBirth;
    private String citizenCode;
    private int pin;
    private Date registrationDate;
    private String idAddress;

    public RegisteredCitizen(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String addres) {
        this.address = addres;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getIdAddress() {
        return idAddress;
    }

    public void setIdAddress(String idAddress) {
        this.idAddress = idAddress;
    }


    @Override
    public String toString(){
        return "RegisteredCitizen{" +
                "name='" + name + "\'" +
                "surname='" + surname + "\'" +
                "id='" + id + "\'" +
                "email'=" + email + "\'" +
                "mobilePhoneNumber='" + mobilePhoneNumber + "\'" +
                "address='" + address + "\'" +
                "dateOfBirth='" + dateOfBirth + "\'" +
                "citizenCode='" + citizenCode + "\'" +
                "pin='" + pin + "\'" +
                "registrationDate='" + registrationDate + "\'" +
                "ideAddress='" + idAddress + "\'" +
                "}";
    }

}
