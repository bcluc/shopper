package com.example.shopper.staffview.product.model;

public class MyColor {
    private String colorName;
    private String colorId;
    private String colorCode;
    private Boolean isChecked;

    public MyColor(String colorName, String colorCode, String colorId, Boolean isChecked) {
        this.colorName = colorName;
        this.colorCode = colorCode;
        this.colorId = colorId;
        this.isChecked = isChecked;
    }
    public MyColor(String colorName, String colorCode, String colorId) {
        this.colorName = colorName;
        this.colorCode = colorCode;
        this.colorId = colorId;
    }
    public MyColor(String colorName)
    {
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }
}
