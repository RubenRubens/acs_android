package com.rubenarriazu.acs.api.requests;

import com.google.gson.annotations.SerializedName;

public class SearchUserRequest {

    @SerializedName("name")
    private String name;

    public SearchUserRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
