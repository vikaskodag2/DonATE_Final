package com.sdl.app.tempdonate.Retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vishvanatarajan on 11/10/17.
 */

public class Profile {

    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("phoneNo")
    private String phoneNo;
    @SerializedName("city")
    private String city;

    public Profile(String name, String email, String phoneNo, String city) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
