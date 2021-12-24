package com.ruzz.butilordering.PaymentFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ruzz.butilordering.Adapter.CartAdapter;
import com.ruzz.butilordering.Adapter.CartItemSelected;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.PaymentActivity;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.ViewModels.CheckoutViewModel;
import com.ruzz.butilordering.ViewModels.CheckoutViewModelFactory;
import com.ruzz.butilordering.ViewModels.PaymentViewModel;
import com.ruzz.butilordering.ViewModels.PaymentViewModelFactory;
import com.ruzz.butilordering.databinding.FragmentOrderInfoBinding;

import java.util.ArrayList;
import java.util.List;


public class OrderInfoFragment extends Fragment implements CartItemSelected {
    private FragmentOrderInfoBinding binding;
    private PaymentViewModel paymentViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        paymentViewModel = new ViewModelProvider(requireActivity(), new PaymentViewModelFactory()).get(PaymentViewModel.class);

        RecyclerView orderItemsView = binding.rvOrderContents;
        orderItemsView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        paymentViewModel.getCurrentOrder().observe(requireActivity(), order -> {
            if (order != null) {
                CartAdapter itemListAdapter = new CartAdapter(this, order.getContents());
                itemListAdapter.setHideDelete();
                orderItemsView.setAdapter(itemListAdapter);
            }
        });

    }

    @Override
    public void removeItem(int pos, ProductModel product) {

    }

    @Override
    public void modifyItem(int pos, ProductModel product) {

    }

    @Override
    public void itemSelected(ProductModel product) {

    }

    @Override
    public ProductModel searchProduct(String uid) {

        ProductModel result = null;
        if (paymentViewModel.getProducts().getValue() != null) {
            List<ProductModel> products = new ArrayList<>(paymentViewModel.getProducts().getValue());
            if (products != null) {
                for (ProductModel item : products) {
                    if (item.getUid().equals(uid)) {
                        result = item;
                    }
                }
            }
        }

        return result;
    }
}