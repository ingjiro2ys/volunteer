package com.example.user.volunteer.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 23/9/2560.
 */

public class QuestionAns {

    @SerializedName("questionID")
    private String questionID;

    @SerializedName("questionName")
    private String questionName;

    @SerializedName("eventID")
    private String eventID;

    @SerializedName("answerID")
    private String answerID;

    @SerializedName("answerDes")
    private String answerDes;

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getAnswerID() {
        return answerID;
    }

    public void setAnswerID(String answerID) {
        this.answerID = answerID;
    }

    public String getAnswerDes() {
        return answerDes;
    }

    public void setAnswerDes(String answerDes) {
        this.answerDes = answerDes;
    }

    @Override
    public String toString() {
        return this.answerDes;
    }
}
