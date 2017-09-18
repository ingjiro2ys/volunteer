package com.example.user.volunteer;

import android.widget.EditText;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 15/9/2560.
 */

public class Answer {

    @SerializedName("questionName")
    private String questionName;

    @SerializedName("eventID")
    private int eventID;

    @SerializedName("questionID")
    private String questionID;


    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    @Override
    public String toString() {
        return this.getQuestionName();
    }

}
