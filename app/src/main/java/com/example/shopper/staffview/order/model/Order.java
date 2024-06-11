package com.example.shopper.staffview.order.model;

import java.util.Date;

public class Order {
    private String userName, phoneNumber, addressId, orderId, discountId, payMethod, state, userId;
    private int discount, transferFee, orderBill, totalBill;
    private Date planDelivered;
    private Date orderDate;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setTransferFee(int transferFee) {
        this.transferFee = transferFee;
    }

    public void setOrderBill(int orderBill) {
        this.orderBill = orderBill;
    }

    public void setTotalBill(int totalBill) {
        this.totalBill = totalBill;
    }

    public void setPlanDelivered(Date planDelivered) {
        this.planDelivered = planDelivered;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public int getDiscount() {
        return discount;
    }

    public int getTransferFee() {
        return transferFee;
    }

    public int getOrderBill() {
        return orderBill;
    }

    public int getTotalBill() {
        return totalBill;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDiscountId() {
        return discountId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getState() {
        return state;
    }

    public Date getPlanDelivered() {
        return planDelivered;
    }

    public Date getOrderDate() {
        return orderDate;
    }


}
