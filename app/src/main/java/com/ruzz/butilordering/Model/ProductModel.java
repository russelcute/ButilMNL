package com.ruzz.butilordering.Model;

public class ProductModel {
    private final String uid, name, image, description, categoryId, type;
    private final double content, price, stocks, storageLife, promo;

    public ProductModel(String id, String name, String image, double stocks, double storageLife, String type,
                        String description, String categoryId, double content, double price, double promo) {
        this.uid = id;
        this.name = name;
        this.image = image;
        this.stocks = stocks;
        this.storageLife = storageLife;
        this.description = description;
        this.type = type;
        this.categoryId = categoryId;
        this.content = content;
        this.price = price;
        this.promo = promo;
    }

    public String getUid() {
        return uid;
    }

    public double getContent() {
        return content;
    }

    public double getPrice() {
        return price;
    }

    public double getStocks() {
        return stocks;
    }

    public double getStorageLife() {
        return storageLife;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getPromo() { return promo; }
}
