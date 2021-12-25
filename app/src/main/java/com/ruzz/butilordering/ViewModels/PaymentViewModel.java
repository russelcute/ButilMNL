package com.ruzz.butilordering.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.Model.ProductModel;

import java.util.List;

public class PaymentViewModel extends ViewModel {
    private MutableLiveData<OrderModel> currentOrder = new MutableLiveData<>();
    private MutableLiveData<List<ProductModel>> products = new MutableLiveData<>();
    private MutableLiveData<String> currentPage = new MutableLiveData<>("OrderInfo");

    public void setCurrentOrder(OrderModel order) {
        this.currentOrder.setValue(order);
    }

    public LiveData<OrderModel> getCurrentOrder() {
        return this.currentOrder;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage.setValue(currentPage);
    }

    public LiveData<String> getCurrentPage() {
        return currentPage;
    }

    public void setProducts(List<ProductModel> products) {
        this.products.setValue(products);
    }

    public LiveData<List<ProductModel>> getProducts() {
        return products;
    }
}
