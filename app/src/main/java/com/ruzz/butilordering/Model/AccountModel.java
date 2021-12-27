package com.ruzz.butilordering.Model;

import java.util.List;

public class AccountModel {
    private String uid, contact, firstName, lastName, gender;
    private List<String> liked;

    public AccountModel() {}

    public AccountModel(String uid, String contact, String firstName, String lastName, String gender) {
        this.uid = uid;
        this.contact = contact;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public void setLiked(List<String> liked) {
        this.liked = liked;
    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContact() {
        return contact;
    }

    public String getGender() {
        return gender;
    }

    public List<String> getLiked() {
        return liked;
    }
}
