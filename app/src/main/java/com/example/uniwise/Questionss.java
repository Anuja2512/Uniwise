package com.example.uniwise;

public class Questionss
{
    public String usernamee, uid, profileImage, question, time, date;
    public Questionss(){

    }
    public Questionss(String usernamee, String uid, String profileImage, String question, String time, String date) {
        this.usernamee = usernamee;
        this.uid = uid;
        this.profileImage = profileImage;
        this.question = question;
        this.time = time;
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsernamee() {
        return usernamee;
    }

    public void setUsernamee(String usernamee) {
        this.usernamee = usernamee;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
