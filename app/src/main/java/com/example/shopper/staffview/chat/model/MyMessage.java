package com.example.shopper.staffview.chat.model;

public class MyMessage {
    private String msgId, senderId, message, time, receiveId;
    long timeStamp;



    public MyMessage(){}
    public MyMessage(String message, String senderId, String receiveId, long timeStamp , String time) {
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.message = message;
        this.receiveId = receiveId;
        this.time = time;
    }
    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
