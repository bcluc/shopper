package com.example.shopper.customerview.home.product.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String productId;
    private String resouceId;
    private String productName;
    private Long productPrice;

    public Product(String resouceId, String productName, String masp, Long productPrice) {
        this.resouceId = resouceId;
        this.productName = productName;
        this.productId = masp;
        this.productPrice = productPrice;
    }
    public Product(String resouceId, String productName, String masp) {
        this.resouceId = resouceId;
        this.productName = productName;
        this.productId = masp;

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String masp) {
        this.productId = masp;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }
}
