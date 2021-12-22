package com.ruzz.butilordering.Model;

public class ProductCartModel {
    private String productId;
    private int quantity;
    private double price;

    public ProductCartModel() {}

    public ProductCartModel(String productId, int quantity, double price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = quantity * price;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
