package com.ruzz.butilordering.Model;

import java.util.Date;
import java.util.List;

public class OrderModel {
    private String userid;
    private UserModel customer;
    private List<ProductCartModel> contents;
    private Double amountDue;
    private Date orderDate;
    private String paymentType;
    private boolean canceled = false;
    private boolean delivered;
    private boolean paid;
    private double latitude, longitude;

    public OrderModel() {}

    public OrderModel(List<ProductCartModel> items) {
        this.contents = items;

        double total = 0;
        for (ProductCartModel item : items) {
            total += item.getPrice();
        }
        this.amountDue = total;
        this.orderDate = new Date();
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setCustomer(UserModel customer) {
        this.customer = customer;
        this.userid = customer.getUid();
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UserModel getCustomer() {
        return customer;
    }

    public List<ProductCartModel> getContents() {
        return contents;
    }

    public Double getAmountDue() {
        return amountDue;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public String getUserid() {
        return userid;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public boolean isPaid() {
        return paid;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
