package com.example.shopper.authentication.model;

import java.io.Serializable;

public class User implements Serializable {
    private String fullName;
    private String status;
    private String email;
    private String phoneNumber;
    private String dayOfBirth;
    // Mã người dùng
    private String maND;
    private  String avatar, diaChi, gioiTinh;
    private String loaiND;
    // Các trường khác mà bạn muốn lưu trong Firestore

    // Tạo constructor và getter/setter
    public User(){

    }
    public User(String fullname, String email, String dayOfBirth, String phoneNumber,
                String MaND, String avatar, String diaChi, String gioiTinh, String Status, String loaiND)
    {
        this.fullName = fullname;
        this.email = email;
        this.dayOfBirth = dayOfBirth;
        this.phoneNumber = phoneNumber;
        this.maND = MaND;
        this.avatar = avatar;
        this.diaChi = diaChi;
        this.gioiTinh = gioiTinh;
        this.status = Status;
        this.loaiND = loaiND;
    }

    public String getLoaiND() {
        return loaiND;
    }

    public void setLoaiND(String loaiND) {
        this.loaiND = loaiND;
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

    public String getMaND() {
        return maND;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getGioiTinh() {
        return gioiTinh;
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

    public void setMaND(String maND) {
        this.maND = maND;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

}