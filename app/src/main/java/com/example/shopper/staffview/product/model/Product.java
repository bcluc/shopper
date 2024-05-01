package com.example.shopper.staffview.product.model;
import java.util.List;

public class Product {
    private String description;
    private String productName;
    private String state;
    private int productPrice;
    private String image;
    private String productId;
    private Object imageUrl;
    private int warehouse; //Tồn kho
    private int love;  //Yêu thích
    private int sold; //Bán ra
    private int views; //Số lượng người xem
    private List<String> productSize;
    private List<String> productColor;

    public Product(String hinhAnhSP, String TenSP, int GiaSP, int warehouse, int sold, int Love, int View, String productId) {
        this.image = hinhAnhSP;
        this.productName = TenSP;
        this.productPrice = GiaSP;
        this.warehouse = warehouse;
        this.sold = sold;
        this.love = Love;
        this.views = View;
        this.productId = productId;
    }

    public Product(String hinhAnhSP, String tenSP, int giaSP) {
        image = hinhAnhSP;
        productName = tenSP;
        productPrice = giaSP;
    }

    public Product() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<String> getProductColor() {
        return productColor;
    }

    public void setProductColor(List<String> productColor) {
        this.productColor = productColor;
    }


    public List<String> getProductSize() {
        return productSize;
    }

    public void setProductSize(List<String> productSize) {
        this.productSize = productSize;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(int warehouse) {
        this.warehouse = warehouse;
    }

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
