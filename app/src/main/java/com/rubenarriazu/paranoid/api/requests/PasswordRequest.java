package com.rubenarriazu.paranoid.api.requests;

public class PasswordRequest {

    private String password;

    public PasswordRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
