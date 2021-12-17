package com.ruzz.butilordering.Model;

public class OrderModel {
    private int Image;
    private String orderStatus;
    private String statusDate;
    private String orderBrand;

    public OrderModel(int image, String orderStatus, String statusDate, String orderBrand) {
        Image = image;
        this.orderStatus = orderStatus;
        this.statusDate = statusDate;
        this.orderBrand = orderBrand;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getOrderBrand() {
        return orderBrand;
    }

    public void setOrderBrand(String orderBrand) {
        this.orderBrand = orderBrand;
    }
}
