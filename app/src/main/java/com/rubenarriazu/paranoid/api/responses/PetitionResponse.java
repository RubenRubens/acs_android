package com.rubenarriazu.acs.api.responses;

import com.google.gson.annotations.SerializedName;

public class PetitionResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("petition_time")
    private String petitionTime;

    @SerializedName("possible_follower")
    private int possibleFollower;

    @SerializedName("user")
    private int user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPetitionTime() {
        return petitionTime;
    }

    public void setPetitionTime(String petitionTime) {
        this.petitionTime = petitionTime;
    }

    public int getPossibleFollower() {
        return possibleFollower;
    }

    public void setPossibleFollower(int possibleFollower) {
        this.possibleFollower = possibleFollower;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
