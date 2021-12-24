package com.ruzz.butilordering.CheckoutFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ruzz.butilordering.Adapter.CartAdapter;
import com.ruzz.butilordering.Adapter.CartItemSelected;
import com.ruzz.butilordering.CheckoutActivity;
import com.ruzz.butilordering.HomeActivity;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.ViewModels.CheckoutViewModel;
import com.ruzz.butilordering.ViewModels.CheckoutViewModelFactory;
import com.ruzz.butilordering.ViewModels.HomeViewModel;
import com.ruzz.butilordering.databinding.FragmentCheckoutBinding;

import java.util.ArrayList;
import java.util.List;


public class CheckoutFragment extends Fragment implements AdapterView.OnItemSelectedListener, CartItemSelected {
    private FragmentCheckoutBinding binding;
    private CheckoutViewModel checkoutViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkoutViewModel = new ViewModelProvider(requireActivity(), new CheckoutViewModelFactory()).get(CheckoutViewModel.class);


        final Spinner paymentSpinner = binding.paymentType;

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(requireActivity(), R.array.Payment,
                android.R.layout.simple_spinner_dropdown_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentSpinner.setOnItemSelectedListener(this);
        paymentSpinner.setAdapter(spinnerAdapter);

        RecyclerView orderItemsView = binding.rvPlaceOrder;
        orderItemsView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        checkoutViewModel.getUserLocation().observe(requireActivity(), place -> {
            binding.verifyAddress.setText(place.getAddress());
        });

        binding.googleLocate.setOnClickListener(v -> {
            checkoutViewModel.setCurerntPage("Location");
        });

        checkoutViewModel.getUserOrder().observe(requireActivity(), order -> {
            if (order != null) {
                CartAdapter itemListAdapter = new CartAdapter(this, order.getContents());
                itemListAdapter.setHideDelete();
                orderItemsView.setAdapter(itemListAdapter);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        checkoutViewModel.setPaymentType(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        List<ProductModel> products = new ArrayList<>(checkoutViewModel.getProducts().getValue());
        ProductModel result = null;

        if (products != null) {
            for (ProductModel item : products) {
                if (item.getUid().equals(uid)) {
                    result = item;
                }
            }
        }

        return result;
    }
}