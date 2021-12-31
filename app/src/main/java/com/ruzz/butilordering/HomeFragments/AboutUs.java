package com.ruzz.butilordering.HomeFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruzz.butilordering.Adapter.CategoriesAdapter;
import com.ruzz.butilordering.Adapter.ProductsAdapter;
import com.ruzz.butilordering.HomeActivity;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.ViewModels.HomeViewModel;
import com.ruzz.butilordering.databinding.FragmentAboutUsBinding;

import java.util.ArrayList;
import java.util.List;

public class AboutUs extends Fragment {
    private FragmentAboutUsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivity)getActivity()).hideCheckOut(true);
    }
}