package com.ruzz.butilordering.Model;

public class CartModel {
    private int cartImage;
    private String title;
    private String brand;
    private String quantity;
    private String discountedPrice;
    private String originalPrice;
    private String description;

    public CartModel(int cartImage, String title, String brand, String quantity, String discountedPrice, String originalPrice, String description) {
        this.cartImage = cartImage;
        this.title = title;
        this.brand = brand;
        this.quantity = quantity;
        this.discountedPrice = discountedPrice;
        this.originalPrice = originalPrice;
        this.description = description;
    }

    public int getCartImage() {
        return cartImage;
    }

    public void setCartImage(int cartImage) {
        this.cartImage = cartImage;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
