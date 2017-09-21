package com.example.user.volunteer.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 21/9/2560.
 */

public class UserRegisEvent {

    @SerializedName("eventID")
    private int eventID;

    @SerializedName("userFName")
    private String userFName;

    @SerializedName("userLname")
    private String userLname;

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getUserFName() {
        return userFName;
    }

    public void setUserFName(String userFName) {
        this.userFName = userFName;
    }

    public String getUserLname() {
        return userLname;
    }

    public void setUserLname(String userLname) {
        this.userLname = userLname;
    }

    @Override
    public String toString() {
        return "UserRegisEvent{" +
                "userFName='" + userFName + '\'' +
                ", userLname='" + userLname + '\'' +
                '}';
    }
}
