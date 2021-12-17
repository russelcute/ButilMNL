package com.ruzz.butilordering.Model;

public class CategoryModel {
    private int categoryImage;
    private String categoryTitle;

    public CategoryModel(int categoryImage, String categoryTitle) {
        this.categoryImage = categoryImage;
        this.categoryTitle = categoryTitle;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
