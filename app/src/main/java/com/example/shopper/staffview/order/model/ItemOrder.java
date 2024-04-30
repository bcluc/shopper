package com.example.shopper.staffview.order.model;

public class ItemOrder {
    private String name;
    private String productColor;
    private String productSize;
    private String image;
    private int price;
    private String orderId;
    private String colorId;
    private String productId;
    private String sizeId;
    private int quanity;

    public ItemOrder(String image, String name, String productId, int price, int quanity) {
        this.price = price;
        this.image = image;
        this.name = name;
        this.productId = productId;
        this.quanity = quanity;
    }

    public ItemOrder(String image, String name, String productId, int price, int quanity, String color, String productSize) {
        this.price = price;
        this.image = image;
        this.name = name;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
