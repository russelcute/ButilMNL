package com.ruzz.butilordering.Model;

import java.util.ArrayList;
import java.util.List;

public class CartModel {
    private double totalPrice;
    private List<ProductCartModel> items;

    public CartModel() {}

    public CartModel(List<ProductCartModel> items) {
        this.items = new ArrayList<>(items);
        double total = 0;
        for (int x=0; x < items.size(); x++) {
            double price = items.get(x).getPrice();
            total += price;
        }
        totalPrice = total;
    }

    public List<ProductCartModel> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
