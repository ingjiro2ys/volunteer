package com.example.user.volunteer.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 22/9/2560.
 */

public class Noti {

    @SerializedName("eventName")
    private String eventName;

    @SerializedName("eventID")
    private int eventID;

    @SerializedName("regisID")
    private int regisID;

    @SerializedName("isJoined")
    private int isJoined;

    @SerializedName("clicked")
    private int clicked;

    @SerializedName("imagePath")
    private String imagePath;

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getRegisID() {
        return regisID;
    }

    public void setRegisID(int regisID) {
        this.regisID = regisID;
    }

    public int getIsJoined() {
        return isJoined;
    }

    public void setIsJoined(int isJoined) {
        this.isJoined = isJoined;
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return this.getEventName();
    }
}
