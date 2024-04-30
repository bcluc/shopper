package com.example.shopper.customerview.home.product.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String productId;
    private String resouceId;
    private String name;
    private Long price;

    public Product(String resouceId, String name, String masp, Long price) {
        this.resouceId = resouceId;
        this.name = name;
        this.productId = masp;
        this.price = price;
    }
    public Product(String resouceId, String name, String masp) {
        this.resouceId = resouceId;
        this.name = name;
        this.productId = masp;

    }

    public String getResouceId() {
        return resouceId;
    }

    public void setResouceId(String resouceId) {
        this.resouceId = resouceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String masp) {
        this.productId = masp;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
