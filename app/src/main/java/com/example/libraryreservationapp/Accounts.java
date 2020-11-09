package com.example.libraryreservationapp;

public class Accounts {

    //private member variables
    private String email;
    private String fName;
    private String lName;
    private String ram_id;
    private String type;
    private Boolean isDisabled;
    private String reason;

    public Accounts(){
        //empty constructor
    }

    public Accounts(String email, String fName, String lName, String ram_id, String type, Boolean isDisabled, String reason) {
        this.email = email;
        this.fName = fName;
        this.lName = lName;
        this.ram_id = ram_id;
        this.type = type;
        this.isDisabled = isDisabled;
        this.reason = reason;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getRam_id() {
        return ram_id;
    }

    public void setRam_id(String ram_id) {
        this.ram_id = ram_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDisabled() {
        return isDisabled;
    }

    public void setDisabled(Boolean disabled) {
        isDisabled = disabled;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
