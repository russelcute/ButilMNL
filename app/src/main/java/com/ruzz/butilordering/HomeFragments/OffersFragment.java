package com.ruzz.butilordering.HomeFragments;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ruzz.butilordering.Adapter.CategoriesAdapter;
import com.ruzz.butilordering.Adapter.CategoriesInterface;
import com.ruzz.butilordering.Adapter.ProductSelected;
import com.ruzz.butilordering.Adapter.ProductsAdapter;
import com.ruzz.butilordering.Adapter.SliderAdapter;
import com.ruzz.butilordering.HomeActivity;
import com.ruzz.butilordering.Model.ProductCartModel;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.ViewModels.HomeViewModel;
import com.ruzz.butilordering.databinding.FragmentOffersBinding;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment implements ProductSelected, CategoriesInterface {
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

        RecyclerView categoriesView = binding.rvCategories;
        categoriesView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));

        homeViewModel.getCategories().observe(requireActivity(), categories -> {
            CategoriesAdapter categoryAdapter = new CategoriesAdapter(this, categories);
            categoriesView.setAdapter(categoryAdapter);
        });

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

        SliderView sliderView = binding.imageScroller;

        homeViewModel.getSlidingImages().observe(requireActivity(), images -> {
            SliderAdapter adapter = new SliderAdapter(images);
            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            sliderView.setSliderAdapter(adapter);
            sliderView.setScrollTimeInSec(3);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();
        });

        binding.etSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    ((HomeActivity)getActivity()).resetProducts();
                } else {
                    ((HomeActivity) getActivity()).searchProduct(s.toString());
                }
            }
        });

    }

    @Override
    public void setSelected(int pos, String uid, boolean liked) {
        homeViewModel.setSelectedProduct(pos);
        ((HomeActivity)getActivity()).gotoProductProfile();
    }

    @Override
    public void changeCategory(String category) {
        if (category.equals("All")) {
            ((HomeActivity)getActivity()).resetProducts();
        } else {
            ((HomeActivity) getActivity()).getFilteredProducts(category);
        }
    }
}