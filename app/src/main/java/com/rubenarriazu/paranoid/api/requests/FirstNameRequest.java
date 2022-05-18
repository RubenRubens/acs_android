package com.rubenarriazu.paranoid.api.requests;

import com.google.gson.annotations.SerializedName;

public class FirstNameRequest {

    @SerializedName("first_name")
    private String firstName;

    public FirstNameRequest(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

}
