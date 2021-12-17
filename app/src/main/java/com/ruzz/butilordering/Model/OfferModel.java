package com.ruzz.butilordering.Model;

public class OfferModel {
    private int OfferImage;
    private String offerPrice;
    private String offerConstraint;

    public OfferModel(int offerImage, String offerPrice, String offerConstraint) {
        OfferImage = offerImage;
        this.offerPrice = offerPrice;
        this.offerConstraint = offerConstraint;
    }

    public int getOfferImage() {
        return OfferImage;
    }

    public void setOfferImage(int offerImage) {
        OfferImage = offerImage;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferConstraint() {
        return offerConstraint;
    }

    public void setOfferConstraint(String offerConstraint) {
        this.offerConstraint = offerConstraint;
    }
}
