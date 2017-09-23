package com.example.user.volunteer.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 22/9/2560.
 */

public class UserRegis {

    @SerializedName("eventID")
    private String eventID;

    @SerializedName("userID")
    private String userID;

    @SerializedName("userFName")
    private String userFName;

    @SerializedName("userLname")
    private String userLname;

    @SerializedName("isJoined")
    private String isJoined;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUserFName() {
        return userFName;
    }

    public void setUserFName(String userFName) {
        this.userFName = userFName;
    }

    public String getUserLName() {
        return userLname;
    }

    public void setUserLName(String userLName) {
        this.userLname = userLName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getIsJoined() {
        return isJoined;
    }

    public void setIsJoined(String isJoined) {
        this.isJoined = isJoined;
    }

    @Override
    public String toString() {
        return this.getUserFName();
    }
}
