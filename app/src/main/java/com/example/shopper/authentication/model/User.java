package com.example.shopper.authentication.model;

import java.io.Serializable;

public class User implements Serializable {
    private String fullName;
    private String status;
    private String email;
    private String phoneNumber;
    private String dayOfBirth;
    // Mã người dùng
    private String userId;
    private  String avatar, address, sex;
    private String userType;
    // Các trường khác mà bạn muốn lưu trong Firestore

    // Tạo constructor và getter/setter
    public User(){

    }
    public User(String fullname, String email, String dayOfBirth, String phoneNumber,
                String userId, String avatar, String address, String sex, String Status, String loaiND)
    {
        this.fullName = fullname;
        this.email = email;
        this.dayOfBirth = dayOfBirth;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
        this.avatar = avatar;
        this.address = address;
        this.sex = sex;
        this.status = Status;
        this.userType = loaiND;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAddress() {
        return address;
    }

    public String getSex() {
        return sex;
    }


    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}