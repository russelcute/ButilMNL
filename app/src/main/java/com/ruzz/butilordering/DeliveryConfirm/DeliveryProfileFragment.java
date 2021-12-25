package com.ruzz.butilordering.DeliveryConfirm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruzz.butilordering.Adapter.CartAdapter;
import com.ruzz.butilordering.Adapter.CartItemSelected;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.ViewModels.DeliverConfirmViewModel;
import com.ruzz.butilordering.ViewModels.DeliverConfirmViewModelFactory;
import com.ruzz.butilordering.databinding.FragmentDeliveryProfileBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DeliveryProfileFragment extends Fragment implements CartItemSelected {

    private FragmentDeliveryProfileBinding binding;
    private DeliverConfirmViewModel deliverConfirmViewModel;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentDeliveryProfileBinding.inflate(inflater, container, false);
        deliverConfirmViewModel = new ViewModelProvider(requireActivity(), new DeliverConfirmViewModelFactory()).get(DeliverConfirmViewModel.class);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView orderContentsView = binding.rvOrderContents;
        orderContentsView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        deliverConfirmViewModel.getCurrentOrder().observe(requireActivity(), order -> {
            String price = "₱" + df.format(order.getAmountDue());
            binding.textAmountDue.setText(price);
            binding.textCustomerAddress.setText(order.getCustomer().getAddress());
            binding.textCustomerContact.setText(order.getCustomer().getContact());
            binding.textCustomerName.setText(order.getCustomer().getUserName());

            CartAdapter itemListAdapter = new CartAdapter(this, order.getContents());
            itemListAdapter.setHideDelete();
            orderContentsView.setAdapter(itemListAdapter);
        });

        deliverConfirmViewModel.getProducts().observe(requireActivity(), products -> {
            OrderModel order = deliverConfirmViewModel.getCurrentOrder().getValue();
            CartAdapter itemListAdapter = new CartAdapter(this, order.getContents());
            itemListAdapter.setHideDelete();
            orderContentsView.setAdapter(itemListAdapter);
        });

        binding.buttonSubmitAddress.setOnClickListener(v -> {
            ((DeliveryConfirmActivity)getActivity()).setDelivered();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
        if (deliverConfirmViewModel.getProducts().getValue() != null) {
            List<ProductModel> products = new ArrayList<>(deliverConfirmViewModel.getProducts().getValue());

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