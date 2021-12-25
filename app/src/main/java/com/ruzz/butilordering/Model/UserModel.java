package com.ruzz.butilordering.Model;

public class UserModel {
    private String userName;
    private String uid;
    private String address;
    private String contact;
    private String gender;
    private String email;
    private Double latitude;
    private Double longitude;

    public UserModel () {}

    public UserModel(String uid, String username, String gender, String email) {
        this.gender = gender;
        this.userName = username;
        this.uid = uid;
        this.email = email;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setLatitudeLongitude(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
