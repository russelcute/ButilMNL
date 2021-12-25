package com.ruzz.butilordering.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruzz.butilordering.Adapter.CartAdapter;
import com.ruzz.butilordering.Adapter.OrderAdapter;
import com.ruzz.butilordering.Adapter.OrderSelected;
import com.ruzz.butilordering.HomeActivity;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.ViewModels.HomeViewModel;
import com.ruzz.butilordering.databinding.FragmentOrderBinding;


public class OrderFragment extends Fragment implements OrderSelected {
    private FragmentOrderBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivity)getActivity()).hideCheckOut(true);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        RecyclerView ordersItemsView = binding.rvOrders;
        ordersItemsView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        homeViewModel.getUserOrders().observe(requireActivity(), orders -> {
            OrderAdapter orderAdapter = new OrderAdapter(this, orders);
            ordersItemsView.setAdapter(orderAdapter);
        });
    }


    @Override
    public void selectedOrder(String productId, String page) {
        ((HomeActivity)getActivity()).gotoOrderActivity(productId, page);
    }
}