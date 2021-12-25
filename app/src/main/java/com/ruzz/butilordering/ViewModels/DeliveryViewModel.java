package com.ruzz.butilordering.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ruzz.butilordering.Model.DeliveryPersonnel;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.Model.UserModel;

import java.util.List;

public class DeliveryViewModel extends ViewModel {
    private MutableLiveData<List<OrderModel>> allDelivery = new MutableLiveData<>();
    private MutableLiveData<UserModel> currentUser = new MutableLiveData<>();
    private MutableLiveData<DeliveryPersonnel> deliveryPersonnel = new MutableLiveData<>();
    private MutableLiveData<OrderModel> selectedOrder = new MutableLiveData<>();
    private MutableLiveData<String> currentPage = new MutableLiveData<>();

    public void setCurrentUser(UserModel currentUser) {
        this.currentUser.setValue(currentUser);
    }

    public LiveData<UserModel> getCurrentUser() {
        return currentUser;
    }

    public void setAllDelivery(List<OrderModel> orders) {
        this.allDelivery.setValue(orders);
    }

    public LiveData<List<OrderModel>> getAllDelivery() {
        return this.allDelivery;
    }

    public void setDeliveryPersonnel(DeliveryPersonnel deliveryPersonnel) {
        this.deliveryPersonnel.setValue(deliveryPersonnel);
    }

    public LiveData<DeliveryPersonnel> getDeliveryPersonnel() {
        return deliveryPersonnel;
    }

    public void setSelectedOrder(OrderModel selectedOrder) {
        this.selectedOrder.setValue(selectedOrder);
    }

    public LiveData<OrderModel> getSelectedOrder() {
        return selectedOrder;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage.setValue(currentPage);
    }

    public LiveData<String> getCurrentPage() {
        return currentPage;
    }
}
