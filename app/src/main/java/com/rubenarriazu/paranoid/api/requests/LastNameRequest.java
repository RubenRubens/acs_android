package com.rubenarriazu.paranoid.api.requests;

import com.google.gson.annotations.SerializedName;

public class LastNameRequest {

    @SerializedName("last_name")
    private String lastName;

    public LastNameRequest(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

}
