package com.rubenarriazu.paranoid.api.responses;

import com.google.gson.annotations.SerializedName;

public class AccountResponse {

    @SerializedName("bio")
    private String bio;

    @SerializedName("profile_picture")
    private String profilePicture;

    @SerializedName("user")
    private int userID;

    public String getBio() {
        return bio;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public int getUserID() {
        return userID;
    }
}
