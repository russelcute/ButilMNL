package com.ruzz.butilordering.Model;

public class ProductCartModel {
    private String productId;
    private int quantity;
    private double price;

    public ProductCartModel() {}

    public ProductCartModel(String productId, int quantity, double price, double promo) {
        this.productId = productId;
        this.quantity = quantity;

        if (promo > 1) {
            double discountedPrice = price - (price * (promo / 100));
            this.price = discountedPrice * quantity;
        } else {
            this.price = quantity * price;
        }

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
