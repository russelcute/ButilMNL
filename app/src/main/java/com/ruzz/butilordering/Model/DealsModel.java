package com.ruzz.butilordering.Model;

public class DealsModel {
    public String dealsImage;
    public String dealsTitle;
    public String dealsType;
    public String dealsStorageLife;
    public String dealsDiscountedPrice;
    public String dealsOriginalPrice;
    public String dealsDiscountedPercentage;

    public DealsModel(String dealsImage, String dealsTitle, String dealsType, String dealsStorageLife, String dealsDiscountedPrice, String dealsOriginalPrice, String dealsDiscountedPercentage) {
        this.dealsImage = dealsImage;
        this.dealsTitle = dealsTitle;
        this.dealsType = dealsType;
        this.dealsStorageLife = dealsStorageLife;
        this.dealsDiscountedPrice = dealsDiscountedPrice;
        this.dealsOriginalPrice = dealsOriginalPrice;
        this.dealsDiscountedPercentage = dealsDiscountedPercentage;
    }

    public String getDealsImage() {
        return dealsImage;
    }

    public void setDealsImage(String dealsImage) {
        this.dealsImage = dealsImage;
    }

    public String getDealsTitle() {
        return dealsTitle;
    }

    public void setDealsTitle(String dealsTitle) {
        this.dealsTitle = dealsTitle;
    }

    public String getDealsType() {
        return dealsType;
    }

    public void setDealsType(String dealsType) {
        this.dealsType = dealsType;
    }

    public String getDealsStorageLife() {
        return dealsStorageLife;
    }

    public void setDealsStorageLife(String dealsStorageLife) {
        this.dealsStorageLife = dealsStorageLife;
    }

    public String getDealsDiscountedPrice() {
        return dealsDiscountedPrice;
    }

    public void setDealsDiscountedPrice(String dealsDiscountedPrice) {
        this.dealsDiscountedPrice = dealsDiscountedPrice;
    }

    public String getDealsOriginalPrice() {
        return dealsOriginalPrice;
    }

    public void setDealsOriginalPrice(String dealsOriginalPrice) {
        this.dealsOriginalPrice = dealsOriginalPrice;
    }

    public String getDealsDiscountedPercentage() {
        return dealsDiscountedPercentage;
    }

    public void setDealsDiscountedPercentage(String dealsDiscountedPercentage) {
        this.dealsDiscountedPercentage = dealsDiscountedPercentage;
    }
}
