package com.sdl.app.tempdonate.Retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vishvanatarajan on 24/10/17.
 */

public class OTP {

    @SerializedName("otp")
    private String otp;

    @SerializedName("email")
    private String email;

    public OTP(String otp, String email) {
        this.otp = otp;
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
