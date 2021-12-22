package com.ruzz.butilordering.Model;

public class UserModel {
    private String userName;
    private String uid;
    private String contact;
    private String gender;
    private String email;

    public UserModel(String uid, String username, String gender, String email) {
        this.gender = gender;
        this.userName = username;
        this.uid = uid;
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public String getUserName() {
        return userName;
    }

    public String getGender() {
        return gender;
    }

    public String getUid() { return uid; }

    public String getEmail() { return email; }
}
