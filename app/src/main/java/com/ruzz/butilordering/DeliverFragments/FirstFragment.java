package com.ruzz.butilordering.DeliverFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ruzz.butilordering.Adapter.DeliveryAdapter;
import com.ruzz.butilordering.Adapter.DeliverySelected;
import com.ruzz.butilordering.DeliverActivity;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.ViewModels.DeliveryViewModel;
import com.ruzz.butilordering.ViewModels.DeliveryViewModelFactory;
import com.ruzz.butilordering.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment implements DeliverySelected {

    private FragmentFirstBinding binding;
    private DeliveryViewModel deliveryViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deliveryViewModel = new ViewModelProvider(requireActivity(), new DeliveryViewModelFactory()).get(DeliveryViewModel.class);
        deliveryViewModel.setCurrentPage("First");

        RecyclerView allDeliveryView = binding.rvMyDelivery;
        allDeliveryView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));

        deliveryViewModel.getDeliveryPersonnel().observe(requireActivity(), personnel -> {
            if (personnel != null) {
                DeliveryAdapter deliverItemAdapter = new DeliveryAdapter(this, personnel.getAssignedDeliver(), true);
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

    }

    @Override
    public void showMap(OrderModel order) {
        ((DeliverActivity)getActivity()).showDeliveryAddress(order);
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_MapsFragment);
    }

    @Override
    public void redirect(String uid) {
        ((DeliverActivity)getActivity()).redirectToDeliverConfirm(uid);
    }
}