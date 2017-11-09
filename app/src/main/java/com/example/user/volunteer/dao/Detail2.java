package com.example.user.volunteer.dao;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by User on 22/9/2560.
 */

public class Detail2 {

    @SerializedName("eventName")
    private String eventName;

    @SerializedName("startDate")
    private Date startDate;

    @SerializedName("endDate")
    private Date endDate;

    @SerializedName("startRegis")
    private Date startRegis;

    @SerializedName("endRegis")
    private Date endRegis;

    @SerializedName("eventPhone")
    private String eventPhone;

    @SerializedName("eventDes1")
    private String eventDes1;

    @SerializedName("joinedAmount")
    private int joinedAmount;

    @SerializedName("eventLocationName")
    private String eventLocationName;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("userOwnerID")
    private int userOwnerID;

    @SerializedName("eventDes2")
    private String eventDes2;

    @SerializedName("imagePath")
    private String imagePath;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartRegis() {
        return startRegis;
    }

    public void setStartRegis(Date startRegis) {
        this.startRegis = startRegis;
    }

    public Date getEndRegis() {
        return endRegis;
    }

    public void setEndRegis(Date endRegis) {
        this.endRegis = endRegis;
    }

    public String getEventPhone() {
        return eventPhone;
    }

    public void setEventPhone(String eventPhone) {
        this.eventPhone = eventPhone;
    }

    public String getEventDes1() {
        return eventDes1;
    }

    public void setEventDes1(String eventDes1) {
        this.eventDes1 = eventDes1;
    }

    public int getJoinedAmount() {
        return joinedAmount;
    }

    public void setJoinedAmount(int joinedAmount) {
        this.joinedAmount = joinedAmount;
    }

    public String getEventLocationName() {
        return eventLocationName;
    }

    public void setEventLocationName(String eventLocationName) {
        this.eventLocationName = eventLocationName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getUserOwnerID() {
        return userOwnerID;
    }

    public void setUserOwnerID(int userOwnerID) {
        this.userOwnerID = userOwnerID;
    }

    public String getEventDes2() {
        return eventDes2;
    }

    public void setEventDes2(String eventDes2) {
        this.eventDes2 = eventDes2;
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
