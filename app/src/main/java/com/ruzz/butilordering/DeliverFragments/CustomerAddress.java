package com.ruzz.butilordering.DeliverFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ruzz.butilordering.DeliverActivity;
import com.ruzz.butilordering.ViewModels.DeliveryViewModel;
import com.ruzz.butilordering.ViewModels.DeliveryViewModelFactory;
import com.ruzz.butilordering.databinding.FragmentCustomerAddressBinding;

public class CustomerAddress extends Fragment {
    private FragmentCustomerAddressBinding binding;
    private DeliveryViewModel deliveryViewModel;
    private GoogleMap gMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;
            getLocation();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCustomerAddressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deliveryViewModel = new ViewModelProvider(requireActivity(), new DeliveryViewModelFactory()).get(DeliveryViewModel.class);
        deliveryViewModel.setCurrentPage("Map");
        ((DeliverActivity)getActivity()).hideAddressBar(false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(binding.getRoot().getId());
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((DeliverActivity)getActivity()).hideAddressBar(true);
        binding = null;
    }

    private void getLocation() {
        deliveryViewModel.getSelectedOrder().observe(requireActivity(), order -> {
            if (order != null) {
                LatLng sydney = new LatLng(order.getCustomer().getLatitude(), order.getCustomer().getLongitude());
                gMap.addMarker(new MarkerOptions().position(sydney).title("Delivery Address"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16.0f));
            }
        });
    }
}