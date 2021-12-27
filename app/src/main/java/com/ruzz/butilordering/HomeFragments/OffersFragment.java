package com.ruzz.butilordering.HomeFragments;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ruzz.butilordering.Adapter.ProductSelected;
import com.ruzz.butilordering.Adapter.ProductsAdapter;
import com.ruzz.butilordering.HomeActivity;
import com.ruzz.butilordering.Model.ProductCartModel;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.ViewModels.HomeViewModel;
import com.ruzz.butilordering.databinding.FragmentOffersBinding;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment implements ProductSelected {
    private FragmentOffersBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOffersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivity)getActivity()).hideCheckOut(true);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        RecyclerView productsView = binding.rvProducts;
        productsView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));

        homeViewModel.getProductList().observe(requireActivity(), products -> {
            if (products != null) {
                List<String> liked = new ArrayList<>();
                if (homeViewModel.getLikedProducts().getValue() != null) {
                    liked = homeViewModel.getLikedProducts().getValue();
                }
                ProductsAdapter productsAdapter = new ProductsAdapter(this, products, liked);
                productsView.setAdapter(productsAdapter);
            }
        });

    }

    @Override
    public void setSelected(int pos, String uid, boolean liked) {
        homeViewModel.setSelectedProduct(pos);
        ((HomeActivity)getActivity()).gotoProductProfile();
    }
}