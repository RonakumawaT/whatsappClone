package com.example.whatsappclone;

public class msgModel {

    private String uid,message,imageUrl,messageId;
    private long timeStamp;


    public msgModel(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }

    public msgModel(){

    }

    public msgModel(String uid, String message, long timeStamp) {
        this.uid = uid;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getUID() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
