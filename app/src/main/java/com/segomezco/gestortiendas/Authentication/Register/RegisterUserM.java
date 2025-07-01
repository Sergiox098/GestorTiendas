package com.segomezco.gestortiendas.Authentication.Register;

public class RegisterUserM {
    private String userName;
    private String email;
    private String phone;
    private String document;
    public RegisterUserM() {}
    public RegisterUserM(String userName, String email, String phone, String document) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.document = document;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getDocument() {
        return document;
    }
    public void setDocument(String document) {
        this.document = document;
    }
}
