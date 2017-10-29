package com.sdl.app.tempdonate.Retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vishvanatarajan on 10/10/17.
 */

public class MSG {

    @SerializedName("success")
    private Boolean success;
    @SerializedName("token")
    private String token;
    @SerializedName("msg")
    private String message;

    /**
     * No args constructor for use in serialization
     */
    public MSG() {
    }

    /**
     * @param success
     * @param token
     */
    public MSG(Boolean success, String token, String message) {
        super();
        this.success = success;
        this.token = token;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
