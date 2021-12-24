package com.ruzz.butilordering.PaymentFragments;

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
import com.ruzz.butilordering.ViewModels.PaymentViewModel;
import com.ruzz.butilordering.ViewModels.PaymentViewModelFactory;
import com.ruzz.butilordering.databinding.FragmentOrderLocationBinding;

public class OrderLocationFragment extends Fragment {
    private FragmentOrderLocationBinding binding;
    private PaymentViewModel paymentViewModel;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            paymentViewModel.getCurrentOrder().observe(requireActivity(), order -> {
                LatLng sydney = new LatLng(order.getLatitude(), order.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        paymentViewModel = new ViewModelProvider(requireActivity(), new PaymentViewModelFactory()).get(PaymentViewModel.class);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(binding.getRoot().getId());
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}