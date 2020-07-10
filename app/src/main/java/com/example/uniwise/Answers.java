package com.example.uniwise;

public class Answers {
    public String uid, answer, date, time, profileimage, username;

    public Answers(){

    }

    public Answers(String uid, String answer, String date, String time, String profileimage, String username) {
        this.uid = uid;
        this.answer = answer;
        this.date = date;
        this.time = time;
        this.profileimage = profileimage;
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
