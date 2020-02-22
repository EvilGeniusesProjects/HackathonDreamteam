package com.example.demoapp.api.chat;

public class Message {
    public static final String CHAT_ID = "chat id";

    private String text;
    private String date;
    private String userImg;
    private String image;
    private String audio;

    public Message(String text, String date, String userImg, String audio) {
        this.text = text;
        this.date = date;
        this.userImg = userImg;
        this.audio = "none";
        if (audio != null)
            this.audio = audio;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getDate() {
        return date;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudio() {
        return audio;
    }

    public Message() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
