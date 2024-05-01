package com.example.shopper.customerview.home.product.model;

public class ProductTrending {
    private String productId;
    private String resouceId;
    private String productName;
    private Integer productPrice;

    public ProductTrending(String productId, String resouceId, String productName, Integer productPrice) {
        this.productId = productId;
        this.resouceId = resouceId;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getResouceId() {
        return resouceId;
    }

    public void setResouceId(String resouceId) {
        this.resouceId = resouceId;
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
}
