package com.example.shopper.staffview.promotion.model;

public class ReadStatus {
    private String maND;
    private boolean read;
    private String tb;
    public ReadStatus() {
        // Required empty constructor for Firestore
    }
    public ReadStatus(String maND, boolean read, String tb) {
        this.maND = maND;
        this.read = read;
        this.tb = tb;
    }

    public String getMaND() {
        return maND;
    }

    public void setMaND(String maND) {
        this.maND = maND;
    }

    // Getter and setter methods (if needed)
}