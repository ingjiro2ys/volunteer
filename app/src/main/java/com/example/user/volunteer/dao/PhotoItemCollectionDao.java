package com.example.user.volunteer.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 3/5/2560.
 */

public class PhotoItemCollectionDao implements Parcelable {
    @SerializedName("Events") private List<PhotoItemDao> events;

    public PhotoItemCollectionDao(){}


    public PhotoItemCollectionDao(Parcel in) {
        events = in.createTypedArrayList(PhotoItemDao.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(events);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoItemCollectionDao> CREATOR = new Creator<PhotoItemCollectionDao>() {
        @Override
        public PhotoItemCollectionDao createFromParcel(Parcel in) {
            return new PhotoItemCollectionDao(in);
        }

        @Override
        public PhotoItemCollectionDao[] newArray(int size) {
            return new PhotoItemCollectionDao[size];
        }
    };

    public List<PhotoItemDao> getEvents() {
        return events;
    }

    public void setEvents(List<PhotoItemDao> events) {
        this.events = events;
    }
}
