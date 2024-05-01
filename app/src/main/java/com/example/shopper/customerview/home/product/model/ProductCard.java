package com.example.shopper.customerview.home.product.model;

public class ProductCard {
    private String productId;
    private String imageResouce;
    private String productName;
    private Integer productPrice;

    public ProductCard() {
    }

    public ProductCard(String imageResouce, String productName, Integer productPrice, String productId) {
        this.imageResouce = imageResouce;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productId = productId;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    public String getImageResouce() {
        return imageResouce;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
