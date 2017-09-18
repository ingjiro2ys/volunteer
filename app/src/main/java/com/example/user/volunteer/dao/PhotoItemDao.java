package com.example.user.volunteer.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by User on 3/5/2560.
 */

public class PhotoItemDao implements Parcelable{

    private int eventID;
    private String eventName;
    private int eventTypeID;
    private Date startDate;
    private Date endDate;
    private int joinedAmount;
    private Date startRegis;
    private Date endRegis;
    private String eventPhone;
    private String eventLocationName;
    private String eventDes1;
    private String eventDes2;
    private int userOwnerID;
    private String verifyImagePath;
    private int isBlacklist;
    private double lat;
    private double lng;
    private String imagePath;
    private String eventTypeName;

    protected PhotoItemDao(Parcel in) {
        eventID = in.readInt();
        eventName = in.readString();
        eventTypeID = in.readInt();
        joinedAmount = in.readInt();
        eventPhone = in.readString();
        eventLocationName = in.readString();
        eventDes1 = in.readString();
        eventDes2 = in.readString();
        userOwnerID = in.readInt();
        verifyImagePath = in.readString();
        isBlacklist = in.readInt();
        lat = in.readDouble();
        lng = in.readDouble();
        imagePath = in.readString();
        eventTypeName = in.readString();
        startDate = new Date(in.readLong());
        endDate = new Date(in.readLong());
        startRegis = new Date(in.readLong());
        endRegis = new Date(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(eventID);
        dest.writeString(eventName);
        dest.writeInt(eventTypeID);
        dest.writeInt(joinedAmount);
        dest.writeString(eventPhone);
        dest.writeString(eventLocationName);
        dest.writeString(eventDes1);
        dest.writeString(eventDes2);
        dest.writeInt(userOwnerID);
        dest.writeString(verifyImagePath);
        dest.writeInt(isBlacklist);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(imagePath);
        dest.writeString(eventTypeName);
        dest.writeLong(startDate.getTime());
        dest.writeLong(endDate.getTime());
        dest.writeLong(startRegis.getTime());
        dest.writeLong(endRegis.getTime());
    }

    public static final Creator<PhotoItemDao> CREATOR = new Creator<PhotoItemDao>() {
        @Override
        public PhotoItemDao createFromParcel(Parcel in) {
            return new PhotoItemDao(in);
        }

        @Override
        public PhotoItemDao[] newArray(int size) {
            return new PhotoItemDao[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventTypeID() {
        return eventTypeID;
    }

    public void setEventTypeID(int eventTypeID) {
        this.eventTypeID = eventTypeID;
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

    public int getJoinedAmount() {
        return joinedAmount;
    }

    public void setJoinedAmount(int joinedAmount) {
        this.joinedAmount = joinedAmount;
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

    public String getEventLocationName() {
        return eventLocationName;
    }

    public void setEventLocationName(String eventLocationName) {
        this.eventLocationName = eventLocationName;
    }

    public String getEventDes1() {
        return eventDes1;
    }

    public void setEventDes1(String eventDes1) {
        this.eventDes1 = eventDes1;
    }

    public String getEventDes2() {
        return eventDes2;
    }

    public void setEventDes2(String eventDes2) {
        this.eventDes2 = eventDes2;
    }

    public int getUserOwnerID() {
        return userOwnerID;
    }

    public void setUserOwnerID(int userOwnerID) {
        this.userOwnerID = userOwnerID;
    }

    public String getVerifyImagePath() {
        return verifyImagePath;
    }

    public void setVerifyImagePath(String verifyImagePath) {
        this.verifyImagePath = verifyImagePath;
    }

    public int getIsBlacklist() {
        return isBlacklist;
    }

    public void setIsBlacklist(int isBlacklist) {
        this.isBlacklist = isBlacklist;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }
}
