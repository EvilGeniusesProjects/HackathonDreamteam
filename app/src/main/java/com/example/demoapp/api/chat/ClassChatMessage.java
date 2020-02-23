package com.example.demoapp.api.chat;

import java.util.Date;

public class ClassChatMessage {
    private String messageText;
    private String uid;
    private long messageTime;

    public ClassChatMessage(String messageText, String uid) {
        this.messageText = messageText;
        this.uid = uid;
        messageTime = new Date().getTime();
    }

    public ClassChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
