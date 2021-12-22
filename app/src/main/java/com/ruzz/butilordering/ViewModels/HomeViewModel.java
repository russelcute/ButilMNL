package com.ruzz.butilordering.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ruzz.butilordering.Model.CartModel;
import com.ruzz.butilordering.Model.ProductCartModel;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<UserModel> currentUser = new MutableLiveData<>();
    private MutableLiveData<List<ProductModel>> productList = new MutableLiveData<>();
    private MutableLiveData<ProductModel> selectedProduct = new MutableLiveData<>();
    private MutableLiveData<CartModel> userCart = new MutableLiveData<>();
    private MutableLiveData<ProductCartModel> selectedItem = new MutableLiveData<>();
    private MutableLiveData<Integer> quantity = new MutableLiveData<>(0);
    private MutableLiveData<String> currentPage = new MutableLiveData<>("Home");
    private MutableLiveData<Double> cartTotalPrice = new MutableLiveData<>();

    public HomeViewModel() {
        UserModel user = new UserModel("Loading...", "Loading...", "Loading...", "Loading...");
        currentUser.setValue(user);
    }

    public void setCurrentUser(UserModel user) {
        currentUser.setValue(user);
    }

    public LiveData<UserModel> getCurrentUser() {
        return currentUser;
    }

    public void setProductList(List<ProductModel> products) {
        productList.setValue(products);
    }

    public LiveData<List<ProductModel>> getProductList() {
        return productList;
    }

    public void setSelectedProduct(int position) {
        List<ProductModel> listProducts = new ArrayList<>(productList.getValue());
        ProductModel selected = listProducts.get(position);
        selectedProduct.setValue(selected);
    }

    public void setSelectedProduct(ProductModel product) {
        selectedProduct.setValue(product);
    }

    public LiveData<ProductModel> getSelectedProduct() {
        return selectedProduct;
    }

    public void setUserCart(CartModel cart) {
        userCart.setValue(cart);
    }

    public LiveData<CartModel> getUserCart() {
        return userCart;
    }

    public void setSelectedItem (int position) {
        List<ProductCartModel> items = new ArrayList<>(userCart.getValue().getItems());
        ProductCartModel item = items.get(position);
        selectedItem.setValue(item);
        quantity.setValue(item.getQuantity());
    }

    public LiveData<ProductCartModel> getSelectedItem() {
        return selectedItem;
    }

    public void addQuantity() {
        double stocks = selectedProduct.getValue().getStocks();
        int orderQuantity = quantity.getValue();
        if (stocks > orderQuantity) {
            orderQuantity += 1;
            quantity.setValue(orderQuantity);
        }
    }

    public void minusQuantity() {
        int orderQuantity = quantity.getValue();
        if (orderQuantity > 1) {
            orderQuantity -= 1;
            quantity.setValue(orderQuantity);
        }
    }

    public LiveData<Integer> getQuantity() {
        return quantity;
    }

    public void setCurrentPage(String page) {
        currentPage.setValue(page);
    }

    public LiveData<String> getCurrentPage() {
        return currentPage;
    }

    public void setCartTotalPrice(double cartTotalPrice) {
        this.cartTotalPrice.setValue(cartTotalPrice);
    }

    public LiveData<Double> getCartTotalPrice() {
        return cartTotalPrice;
    }
}
