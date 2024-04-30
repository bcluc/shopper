package com.example.shopper.customerview.home.product.model;

public class ProductCard {
    private String productId;
    private String imageResouce;
    private String nameProduct;
    private Integer priceProduct;

    public ProductCard() {
    }

    public ProductCard(String imageResouce, String nameProduct, Integer priceProduct, String productId) {
        this.imageResouce = imageResouce;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.productId = productId;
    }


    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Integer getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(Integer priceProduct) {
        this.priceProduct = priceProduct;
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
