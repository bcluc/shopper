package com.example.shopper.customerview.home.category.model;

public class Categories {
    private String categoryId;
    private String image;
    private String name;
    public Categories(String name, String image, String categoryId) {
        this.name = name;
        this.image = image;
        this.categoryId = categoryId;

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
