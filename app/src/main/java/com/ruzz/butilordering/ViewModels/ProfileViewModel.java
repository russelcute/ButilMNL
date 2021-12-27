package com.ruzz.butilordering.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ruzz.butilordering.Model.CartModel;
import com.ruzz.butilordering.Model.ProductCartModel;
import com.ruzz.butilordering.Model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<ProductModel> selectedProduct = new MutableLiveData<>();
    private MutableLiveData<List<String>> likedProducts = new MutableLiveData<>();
    private MutableLiveData<List<ProductModel>> featuredProducts = new MutableLiveData<>();
    private MutableLiveData<ProductCartModel> currentItem = new MutableLiveData<>();
    private MutableLiveData<CartModel> userCart = new MutableLiveData<>();
    private String userId = "";
    private Boolean hasCart = false;
    private double currentTotal = 0;
    private MutableLiveData<Integer> quantity = new MutableLiveData<>(1);
    private MutableLiveData<Boolean> addedToCart = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> likedProduct = new MutableLiveData<>(false);

    public void setSelectedProduct(ProductModel product) {
        selectedProduct.setValue(product);
        if (userCart.getValue() != null && userCart.getValue().getItems() != null) {
            List<ProductCartModel> products = new ArrayList<>(userCart.getValue().getItems());

            for (ProductCartModel item : products) {
                if (item.getProductId().equals(product.getUid())) {
                    currentItem.setValue(item);
                    addedToCart.setValue(true);
                }
            }
        }
    }

    public LiveData<ProductModel> getSelectedProduct() {
        return selectedProduct;
    }

    public void setFeaturedProducts(List<ProductModel> featured) {
        featuredProducts.setValue(featured);
    }

    public LiveData<List<ProductModel>> getFeaturedProducts() {
        return featuredProducts;
    }

    public void setSelectedFeatured(int position) {
        List<ProductModel> listProducts = new ArrayList<>(featuredProducts.getValue());
        ProductModel selected = listProducts.get(position);
        selectedProduct.setValue(selected);
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

    public void resetQuantity() {
        quantity.setValue(1);
    }

    public LiveData<Integer> getQuantity() {
        return quantity;
    }

    public void setUserId(String id) {
        userId = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setHasCart(boolean value) {
        this.hasCart = value;
     }

    public Boolean getHasCart() {
        return hasCart;
    }

    public void setCurrentTotal(double currentTotal) {
        this.currentTotal = currentTotal;
    }

    public double getCurrentTotal() {
        return currentTotal;
    }

    public LiveData<ProductCartModel> getCurrentItem () {
        return currentItem;
    }

    public void setUserCart(CartModel cart) {
        userCart.setValue(cart);
        if (selectedProduct.getValue() != null && cart.getItems() != null) {
            List<ProductCartModel> items = new ArrayList<>(cart.getItems());
            for (ProductCartModel item : items) {
                if (item.getProductId().equals(selectedProduct.getValue().getUid())) {
                    currentItem.setValue(item);
                    addedToCart.setValue(true);
                }
            }
        }
    }

    public LiveData<CartModel> getUserCart() {
        return userCart;
    }

    public void setAddedToCart(boolean value) {
        this.addedToCart.setValue(value);
    }

    public LiveData<Boolean> getAddedToCart() {
        return addedToCart;
    }

    public void setLikedProduct(boolean value) {
        this.likedProduct.setValue(value);
    }

    public LiveData<Boolean> getLikedProduct() {
        return likedProduct;
    }

    public void setLikedProducts(List<String> liked) {
        likedProducts.setValue(liked);
    }

    public LiveData<List<String>> getLikedProducts() {
        return likedProducts;
    }
}
