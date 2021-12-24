package com.ruzz.butilordering.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ruzz.butilordering.Model.CartModel;
import com.ruzz.butilordering.Model.LocationModel;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.Model.UserModel;

import java.util.List;

public class CheckoutViewModel extends ViewModel {
    private MutableLiveData<CartModel> userCart = new MutableLiveData<>();
    private MutableLiveData<UserModel> currentUser = new MutableLiveData<>();
    private MutableLiveData<OrderModel> userOrder = new MutableLiveData<>();
    private MutableLiveData<List<ProductModel>> products = new MutableLiveData<>();
    private MutableLiveData<LocationModel> userLocation = new MutableLiveData<>();
    private MutableLiveData<String> curerntPage = new MutableLiveData<>("PlaceOrder");
    private MutableLiveData<String> paymentType = new MutableLiveData<>("Gcash");

    public void setCurrentUser(UserModel user) {
        currentUser.setValue(user);
    }

    public LiveData<UserModel> getCurrentUser() {
        return currentUser;
    }

    public void setUserCart(CartModel cart) {
        userCart.setValue(cart);
    }

    public LiveData<CartModel> getUserCart() {
        return userCart;
    }

    public void setUserOrder(OrderModel order) {
        userOrder.setValue(order);
    }

    public LiveData<OrderModel> getUserOrder() {
        return userOrder;
    }

    public void setProducts(List<ProductModel> products) {
        this.products.setValue(products);
    }

    public LiveData<List<ProductModel>> getProducts() {
        return products;
    }

    public void setUserLocation(LocationModel location) {
        userLocation.setValue(location);
    }

    public LiveData<LocationModel> getUserLocation() {
        return userLocation;
    }

    public void setCurerntPage(String page) {
        curerntPage.setValue(page);
    }

    public LiveData<String> getCurrentPage() {
        return curerntPage;
    }

    public void setPaymentType(String payment) {
        paymentType.setValue(payment);
    }

    public LiveData<String> getPaymentType() {
        return paymentType;
    }
}
