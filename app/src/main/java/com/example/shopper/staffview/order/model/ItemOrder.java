package com.example.shopper.staffview.order.model;

public class ItemOrder {
    private String productName;
    private String productColor;
    private String productSize;
    private String image;
    private int productPrice;
    private String orderId;
    private String colorId;
    private String productId;
    private String sizeId;
    private int quanity;

    public ItemOrder(String image, String productName, String productId, int productPrice, int quanity) {
        this.productPrice = productPrice;
        this.image = image;
        this.productName = productName;
        this.productId = productId;
        this.quanity = quanity;
    }

    public ItemOrder(String image, String productName, String productId, int productPrice, int quanity, String color, String productSize) {
        this.productPrice = productPrice;
        this.image = image;
        this.productName = productName;
        this.productId = productId;
        this.quanity = quanity;
        this.productColor = color;
        this.productSize = productSize;
    }


    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

}
