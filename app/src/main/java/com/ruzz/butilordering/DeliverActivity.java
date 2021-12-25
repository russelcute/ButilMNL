package com.ruzz.butilordering;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruzz.butilordering.DeliverFragments.FirstFragment;
import com.ruzz.butilordering.DeliveryConfirm.DeliveryConfirmActivity;
import com.ruzz.butilordering.Model.DeliveryPersonnel;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.Model.UserModel;
import com.ruzz.butilordering.ViewModels.DeliveryViewModel;
import com.ruzz.butilordering.ViewModels.DeliveryViewModelFactory;
import com.ruzz.butilordering.databinding.ActivityDeliverBinding;

import java.util.ArrayList;
import java.util.List;

public class DeliverActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityDeliverBinding binding;
    private DeliveryViewModel deliveryViewModel;
    private FirebaseAuth appAuth;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDeliverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        deliveryViewModel = new ViewModelProvider(this, new DeliveryViewModelFactory()).get(DeliveryViewModel.class);
        database = FirebaseFirestore.getInstance();
        appAuth = FirebaseAuth.getInstance();
        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_deliver);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.fab.setOnLongClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            goToLoginActivity();
            return false;
        });

        deliveryViewModel.getCurrentPage().observe(this, page -> {
            if (page.equals("First")) {
                binding.fab.setVisibility(View.VISIBLE);
            } else {
                binding.fab.setVisibility(View.GONE);
            }
        });

        hideAddressBar(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = appAuth.getCurrentUser();

        if (currentUser == null) {
            goToLoginActivity();
        }

        DocumentReference docRef = database.collection("accounts").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot result = task.getResult();
                if (result.exists()) {
                    String username = result.getString("firstName") + " " + result.getString("lastName");
                    String gender = result.getString("gender");
                    String contact = result.getString("contact");

                    UserModel user = new UserModel(currentUser.getUid(), username, gender, currentUser.getEmail());
                    user.setContact(contact);

                   deliveryViewModel.setCurrentUser(user);

                } else {
                    Toast.makeText(DeliverActivity.this, "User does not exist.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DeliverActivity.this, "Failed to fetch user.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        deliveryViewModel.getSelectedOrder().observe(this, order -> {
            if (order != null) {
                binding.textAddressDisplay.setText(order.getCustomer().getAddress());
            }
        });

        fetchAllDelivery();
        fetchDeliveryPersonnel(currentUser.getUid());

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_deliver);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void hideAddressBar(boolean value) {
        binding.deliverLocationDialog.setVisibility(value ? View.GONE : View.VISIBLE);
    }

    private void fetchAllDelivery() {
        database.collection("orders").whereEqualTo("assigned", false)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<OrderModel> userOrders = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            OrderModel order = document.toObject(OrderModel.class);
                            order.setUserid(document.getId());
                            userOrders.add(order);
                        }

                        deliveryViewModel.setAllDelivery(userOrders);
                    }
                });
    }

    private void fetchDeliveryPersonnel(String uid) {
        database.collection("delivery").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                DeliveryPersonnel personnel = document.toObject(DeliveryPersonnel.class);
                                if (personnel.getAssignedDeliver() != null) {
                                    deliveryViewModel.setDeliveryPersonnel(personnel);
                                }
                            } else {
                                FirebaseAuth.getInstance().signOut();
                                goToLoginActivity();
                            }
                        }
                    }
                });
    }

    public void assignDelivery(OrderModel order) {
        DeliveryPersonnel personnel = deliveryViewModel.getDeliveryPersonnel().getValue();
        UserModel currentUser = deliveryViewModel.getCurrentUser().getValue();

        if (personnel != null) {
            database.collection("delivery").document(currentUser.getUid())
                    .update("assignedDeliver", FieldValue.arrayUnion(order));

            database.collection("orders").document(order.getUserid())
                    .update("assigned", true);
        } else {
            List<OrderModel> assigned = new ArrayList<>();
            assigned.add(order);

            DeliveryPersonnel newPersonnel = new DeliveryPersonnel(assigned);

            database.collection("delivery").document(currentUser.getUid())
                    .set(newPersonnel);

            database.collection("orders").document(order.getUserid())
                    .update("assigned", true);

            deliveryViewModel.setDeliveryPersonnel(newPersonnel);
        }

        fetchAllDelivery();
        fetchDeliveryPersonnel(currentUser.getUid());
    }

    public void showDeliveryAddress(OrderModel order) {
        deliveryViewModel.setSelectedOrder(order);
    }

    public void redirectToDeliverConfirm(String uid) {
        Intent intent = new Intent(this, DeliveryConfirmActivity.class);
        intent.putExtra("Order", uid);
        startActivity(intent);
    }
}