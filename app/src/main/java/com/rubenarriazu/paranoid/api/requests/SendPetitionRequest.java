package com.rubenarriazu.acs.api.requests;

import com.google.gson.annotations.SerializedName;

public class SendPetitionRequest {

    @SerializedName("user")
    private int user;

    public SendPetitionRequest(int user) {
        this.user = user;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

}
