package com.example.shopper.customerview.home.product.model;

public class ProductTrending {
    private String productId;
    private String resouceId;
    private String name;
    private Integer priceProduct;

    public ProductTrending(String productId, String resouceId, String name, Integer priceProduct) {
        this.productId = productId;
        this.resouceId = resouceId;
        this.name = name;
        this.priceProduct = priceProduct;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(Integer priceProduct) {
        this.priceProduct = priceProduct;
    }
}
