package com.ruzz.butilordering.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ruzz.butilordering.Model.DeliveryPersonnel;
import com.ruzz.butilordering.Model.LocationModel;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.Model.UserModel;

import java.util.List;

public class DeliverConfirmViewModel extends ViewModel {
    private MutableLiveData<OrderModel> currentOrder = new MutableLiveData<>();
    private MutableLiveData<UserModel> currentUser = new MutableLiveData<>();
    private MutableLiveData<List<ProductModel>> products = new MutableLiveData<>();
    private MutableLiveData<Double> change = new MutableLiveData<>(0.00);
    private MutableLiveData<DeliveryPersonnel> personnel = new MutableLiveData<>();
    private MutableLiveData<LocationModel> orderLocation = new MutableLiveData<>();

    public void setCurrentOrder(OrderModel order) {
        this.currentOrder.setValue(order);
    }

    public void setCurrentUser(UserModel user) {
        this.currentUser.setValue(user);
    }

    public LiveData<OrderModel> getCurrentOrder() {
        return this.currentOrder;
    }

    public LiveData<UserModel> getCurrentUser() {
        return currentUser;
    }

    public void setProducts(List<ProductModel> products) {
        this.products.setValue(products);
    }

    public LiveData<List<ProductModel>> getProducts() {
        return products;
    }

    public void setPersonnel(DeliveryPersonnel personnel) {
        this.personnel.setValue(personnel);
    }

    public LiveData<DeliveryPersonnel> getDeliveryPersonnel() {
        return personnel;
    }

    public void setChange(double change) {
        Double amount = currentOrder.getValue().getAmountDue();
        if (change > amount) {
            this.change.setValue(change - amount);
        }
    }

    public LiveData<Double> getChange() {
        return change;
    }

    public void setOrderLocation(LocationModel location) {
        this.orderLocation.setValue(location);
    }

    public LiveData<LocationModel> getOrderLocation() {
        return orderLocation;
    }
}
