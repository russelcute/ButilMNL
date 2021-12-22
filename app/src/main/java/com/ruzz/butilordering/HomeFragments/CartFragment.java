package com.ruzz.butilordering.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruzz.butilordering.Adapter.CartAdapter;
import com.ruzz.butilordering.Adapter.CartItemSelected;
import com.ruzz.butilordering.Adapter.ProductsAdapter;
import com.ruzz.butilordering.HomeActivity;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.ViewModels.HomeViewModel;
import com.ruzz.butilordering.databinding.FragmentCartBinding;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartItemSelected {
    private FragmentCartBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivity)getActivity()).hideCheckOut(false);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        RecyclerView cartItemsView = binding.rvCart;
        cartItemsView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        homeViewModel.getUserCart().observe(requireActivity(), item -> {
            if (item != null) {
                CartAdapter itemListAdapter = new CartAdapter(this, item.getItems());
                cartItemsView.setAdapter(itemListAdapter);
            }
        });
    }

    @Override
    public void removeItem(int pos, ProductModel product) {
        homeViewModel.setSelectedProduct(product);
        homeViewModel.setSelectedItem(pos);
        ((HomeActivity)getActivity()).removeItemCart();
    }

    @Override
    public void modifyItem(int pos, ProductModel product) {
        homeViewModel.setSelectedProduct(product);
        homeViewModel.setSelectedItem(pos);
        ((HomeActivity)getActivity()).showButtomSheetDialog();
    }

    @Override
    public void itemSelected(ProductModel product) {
        homeViewModel.setSelectedProduct(product);
        ((HomeActivity)getActivity()).gotoSelectedProduct(product.getUid());
    }

    @Override
    public ProductModel searchProduct(String uid) {
        List<ProductModel> products = new ArrayList<>(homeViewModel.getProductList().getValue());
        ProductModel result = null;
        for (ProductModel product : products) {
            if (product.getUid().equals(uid)) {
                result = product;
            }
        }

        return result;
    }
}