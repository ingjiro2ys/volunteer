package com.example.user.volunteer.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 27/10/2560.
 */

public class Comment {

    @SerializedName("eventID")
    private String eventID;

    @SerializedName("userCommentID")
    private String userCommentID;

    @SerializedName("description")
    private String description;

    @SerializedName("userFName")
    private String userFName;

    @SerializedName("userLname")
    private String userLname;

    @SerializedName("userName")
    private String userName;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUserCommentID() {
        return userCommentID;
    }

    public void setUserCommentID(String userCommentID) {
        this.userCommentID = userCommentID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return this.getDescription();
    }
}
