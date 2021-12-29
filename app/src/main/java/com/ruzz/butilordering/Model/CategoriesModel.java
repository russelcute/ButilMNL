package com.ruzz.butilordering.Model;

public class CategoriesModel {
    private String title;
    private String image;

    public CategoriesModel() {}

    public CategoriesModel(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
