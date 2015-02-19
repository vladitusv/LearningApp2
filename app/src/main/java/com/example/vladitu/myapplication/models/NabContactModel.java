package com.example.vladitu.myapplication.models;

/**
 * Created by vlad.itu on 18-Feb-15.
 */
public class NabContactModel {
    private String displayName;
    private String phoneNumber;

    public NabContactModel(String displayName, String phoneNumber) {
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
