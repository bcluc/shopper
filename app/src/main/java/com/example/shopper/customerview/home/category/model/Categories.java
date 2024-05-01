package com.example.shopper.customerview.home.category.model;

public class Categories {
    private String categoryId;
    private String categoryImage;
    private String categoryName;
    public Categories(String categoryName, String categoryImage, String categoryId) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
