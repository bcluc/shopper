package com.example.shopper.customerview.util.color.model;

public class Colors {
    private String colorId;
    private String colorCode;
    private String colorName;

    public Colors(String colorId, String colorCode, String colorName) {
        this.colorId = colorId;
        this.colorCode = colorCode;
        this.colorName = colorName;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
