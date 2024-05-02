package com.example.shopper.staffview.category.model;


public class MyCategory {
    private String categoryName;
    private String categoryImage;
    private int quantity;
    private String categoryId;
    private boolean isSelected;

    public MyCategory(String categoryName, boolean isSelected, String categoryId, int quantity) {
        this.categoryName = categoryName;
        this.isSelected = isSelected;
        this.categoryId = categoryId;
        this.quantity = quantity;
    }

    public MyCategory(String categoryName, String categoryImage, int quantity) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.quantity = quantity;
    }

    public MyCategory(String categoryName, String categoryImage, int quantity, boolean isSelected) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.quantity = quantity;
        this.isSelected = isSelected;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
