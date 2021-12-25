package com.ruzz.butilordering.HomeFragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ruzz.butilordering.Adapter.ProductSelected;
import com.ruzz.butilordering.Adapter.ProductsAdapter;
import com.ruzz.butilordering.LoginActivity;
import com.ruzz.butilordering.Model.CartModel;
import com.ruzz.butilordering.Model.ProductCartModel;
import com.ruzz.butilordering.ProductProfile;
import com.ruzz.butilordering.ViewModels.ProfileViewModel;
import com.ruzz.butilordering.ViewModels.ProfileViewModelFactory;
import com.ruzz.butilordering.databinding.FragmentProductProfileBinding;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductProfileFragment extends Fragment implements ProductSelected {
    private FragmentProductProfileBinding binding;
    private ProfileViewModel productViewModel;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProductProfileBinding.inflate(inflater, container, false);
        binding.productProfile.setVisibility(View.INVISIBLE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productViewModel = new ViewModelProvider(requireActivity(), new ProfileViewModelFactory()).get(ProfileViewModel.class);

       productViewModel.getSelectedProduct().observe(requireActivity(), product -> {
            if (product != null) {
                String title = product.getName() + " " + "(" + (int) product.getContent() + "g" + ")";
                binding.txtDealsTitle.setText(title);

                String price = "₱" + df.format(product.getPrice());
                if (product.getPromo() > 1) {
                    double discountedPrice = product.getPrice() - (product.getPrice() * (product.getPromo() / 100));
                    String priceDiscount = "₱" + df.format(discountedPrice);
                    TextView priceView = binding.txtDealsPrice;
                    priceView.setText(price);
                    priceView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    binding.txtDealsPromo.setVisibility(View.VISIBLE);
                    binding.txtDealsPromo.setText(priceDiscount);
                } else {
                    binding.txtDealsPromo.setVisibility(View.GONE);
                    binding.txtDealsPrice.setText(price);
                }

                if (product.getType() != null && !product.getType().equals("Standard")) {
                    String type = product.getType() + " " + product.getCategoryId();
                    binding.txtDealsType.setText(type);
                } else {
                    binding.txtDealsType.setText(product.getCategoryId());
                }

                binding.txtDescription.setText(product.getDescription());
                String storage = (int) product.getStorageLife() + " days storage life";
                binding.txtStorageLife.setText(storage);
                Picasso.get().load(product.getImage()).into(binding.rvDealsImage);

                if (product.getPromo() == 0) {
                    binding.txtDealsDiscountedPercentage.setVisibility(View.GONE);
                } else {
                    String promo = "—" + (int) product.getPromo() + "%";
                    binding.txtDealsDiscountedPercentage.setText(promo);
                }

                binding.btnCartCheck.setVisibility(View.GONE);
            }
        });

        productViewModel.getCurrentItem().observe(requireActivity(), item -> {
            if (item != null) {
                binding.btnCartAdd.setVisibility(View.GONE);
                binding.btnCartCheck.setVisibility(View.VISIBLE);
            }
        });

        RecyclerView productsView = binding.rvProducts;
        productsView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));

        productViewModel.getFeaturedProducts().observe(requireActivity(), products -> {
            if (products != null) {
                ProductsAdapter productsAdapter = new ProductsAdapter(this, products);
                productsView.setAdapter(productsAdapter);
            }
        });

        binding.productProfile.setVisibility(View.VISIBLE);
        binding.btnCartAdd.setOnClickListener(v -> {
            ((ProductProfile)getActivity()).showButtomSheetDialog();
        });

    }

    @Override
    public void setSelected(int pos, String uid) {
        productViewModel.setSelectedFeatured(pos);
        productViewModel.resetQuantity();
        ((ProductProfile)getActivity()).gotoProductProfile();
    }
}