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

import com.google.firebase.firestore.FirebaseFirestore;
import com.ruzz.butilordering.Adapter.OrderAdapter;
import com.ruzz.butilordering.HomeActivity;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.ViewModels.HomeViewModel;
import com.ruzz.butilordering.ViewModels.HomeViewModelFactory;
import com.ruzz.butilordering.databinding.FragmentUserProfiileBinding;

public class UserProfiileFragment extends Fragment {
    private FragmentUserProfiileBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfiileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(requireActivity(), new HomeViewModelFactory()).get(HomeViewModel.class);

        homeViewModel.getCurrentUser().observe(requireActivity(), user -> {
            if (user != null) {
                binding.customerName.setText(user.getUserName());
                binding.contactNUmber.setText(user.getContact());
                binding.customerEmail.setText(user.getEmail());
                binding.gender.setText(user.getGender());
            }
        });

        binding.btnSaveChanges.setOnClickListener(v -> {
            String username = binding.customerName.getText().toString();
            String contact = binding.contactNUmber.getText().toString();

            ((HomeActivity)getActivity()).saveProfileChanges(username, contact);
        });
    }
}