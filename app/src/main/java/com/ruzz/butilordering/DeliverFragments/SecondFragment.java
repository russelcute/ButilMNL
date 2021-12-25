package com.ruzz.butilordering.DeliverFragments;

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

import com.ruzz.butilordering.Adapter.DeliveryAdapter;
import com.ruzz.butilordering.Adapter.DeliverySelected;
import com.ruzz.butilordering.Adapter.OrderSelected;
import com.ruzz.butilordering.DeliverActivity;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.ViewModels.DeliveryViewModel;
import com.ruzz.butilordering.ViewModels.DeliveryViewModelFactory;
import com.ruzz.butilordering.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment implements DeliverySelected {

    private FragmentSecondBinding binding;
    private DeliveryViewModel deliveryViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deliveryViewModel = new ViewModelProvider(requireActivity(), new DeliveryViewModelFactory()).get(DeliveryViewModel.class);
        deliveryViewModel.setCurrentPage("Second");

        RecyclerView allDeliveryView = binding.rvAllDelivery;
        allDeliveryView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        deliveryViewModel.getAllDelivery().observe(requireActivity(), deliveries -> {
            if (deliveries != null) {
                DeliveryAdapter deliverItemAdapter = new DeliveryAdapter(this, deliveries, false);
                allDeliveryView.setAdapter(deliverItemAdapter);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void assign(OrderModel order) {
        ((DeliverActivity)getActivity()).assignDelivery(order);
    }

    @Override
    public void showMap(OrderModel order) {
        ((DeliverActivity)getActivity()).showDeliveryAddress(order);
        NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_MapsFragment);
    }

    @Override
    public void redirect(String uid) {

    }
}