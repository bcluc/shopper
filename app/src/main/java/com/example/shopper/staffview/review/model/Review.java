package com.example.shopper.staffview.review.model;

public class Review {

    private String avatar;
    private String name;
    private String time;
    private float rating;
    private String content;
    private String image;

    public Review(String avatar, String name, String time, float rating, String content, String image) {
        this.avatar = avatar;
        this.name = name;
        this.time = time;
        this.rating = rating;
        this.content = content;
        this.image = image;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

