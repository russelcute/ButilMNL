package com.ruzz.butilordering.CheckoutFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ruzz.butilordering.Adapter.GeoLocation;
import com.ruzz.butilordering.CheckoutActivity;
import com.ruzz.butilordering.Model.LocationModel;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.ViewModels.CheckoutViewModel;
import com.ruzz.butilordering.ViewModels.CheckoutViewModelFactory;
import com.ruzz.butilordering.databinding.DialogLocationBottomBinding;
import com.ruzz.butilordering.databinding.FragmentUserAddressBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserAddressFragment extends Fragment implements GeoLocation {
    private FragmentUserAddressBinding binding;
    private GoogleMap gMap;
    private FusedLocationProviderClient clientLocation;
    private CheckoutViewModel checkoutViewModel;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            gMap = googleMap;
            getCurrentLocation();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentUserAddressBinding.inflate(inflater, container, false);
        ((CheckoutActivity)getActivity()).setListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkoutViewModel = new ViewModelProvider(requireActivity(), new CheckoutViewModelFactory()).get(CheckoutViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(binding.getRoot().getId());
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        clientLocation = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(requireActivity(), "Access to location denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestLocationPermission() {
        String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION };
        ActivityCompat.requestPermissions(requireActivity(), permissions, 1);
    }

    private void getCurrentLocation () {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            clientLocation.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.getResult() != null) {
                                LatLng userLocation = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
                                gMap.addMarker(new MarkerOptions().position(userLocation).title("You are currently here!"));
                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16.0f));
                                Geocoder geocoder = new Geocoder(requireActivity().getApplicationContext(), Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(task.getResult().getLatitude(), task.getResult().getLongitude(), 1);
                                    String address = addresses.get(0).getAddressLine(0);

                                    LocationModel location = new LocationModel(address, task.getResult().getLatitude(), task.getResult().getLongitude());

                                    checkoutViewModel.setUserLocation(location);

                                } catch (IOException e) {
                                    Toast.makeText(requireActivity(), e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                AlertDialog.Builder gpsAlert = new AlertDialog.Builder((CheckoutActivity)getActivity());

                                gpsAlert.setMessage(R.string.gps_dialog_message).setTitle(R.string.gps_dialog_title);

                                gpsAlert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getLocation();
                                    }
                                });

                                gpsAlert.create();
                                gpsAlert.show();
                            }
                        }
                    });
        } else {
            requestLocationPermission();
        }
    }

    @Override
    public void getLocation() {
        getCurrentLocation();
    }
}