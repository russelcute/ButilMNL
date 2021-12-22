package com.ruzz.butilordering.Adapter;

import com.ruzz.butilordering.Model.ProductModel;

public interface CartItemSelected {
    void removeItem(int pos, ProductModel product);
    void modifyItem(int pos, ProductModel product);
    void itemSelected(ProductModel product);
    ProductModel searchProduct(String uid);
}
