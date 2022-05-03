package com.rubenarriazu.acs.api.requests;

import com.google.gson.annotations.SerializedName;

public class AcceptPetitionRequest {

    @SerializedName("possible_follower")
    private int followerId;

    public AcceptPetitionRequest(int followerId) {
        this.followerId = followerId;
    }

    public int getFollowerId() {
        return followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

}
