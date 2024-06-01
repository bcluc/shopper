package com.example.shopper.staffview.chat.model;

public class MyMessage {
    private String msgId, senderId, message, time, receiveID;
    long timestamp;



    public MyMessage(){}
    public MyMessage(String message, String senderId, String receiveID, long timestamp , String time) {
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.message = message;
        this.receiveID = receiveID;
        this.time = time;
    }
    public String getReceiveID() {
        return receiveID;
    }

    public void setReceiveID(String receiveID) {
        this.receiveID = receiveID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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
