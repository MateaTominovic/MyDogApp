package com.sbgmail.mateatominovic.mydogapp.activities.dataModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Enigma on 10.9.2017..
 */
public class Confing {

    String username;
    boolean status;
    String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
