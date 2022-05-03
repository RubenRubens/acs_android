package com.rubenarriazu.paranoid.api.responses;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("key")
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
