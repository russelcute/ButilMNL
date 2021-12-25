package com.ruzz.butilordering.Adapter;

import com.ruzz.butilordering.Model.OrderModel;

public interface DeliverySelected {
    void assign(OrderModel order);
    void showMap(OrderModel order);
    void redirect(String uid);
}
