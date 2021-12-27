package com.ruzz.butilordering.DeliveryConfirm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.ruzz.butilordering.Adapter.GeoLocation;
import com.ruzz.butilordering.CheckoutActivity;
import com.ruzz.butilordering.DeliverActivity;
import com.ruzz.butilordering.LoginActivity;
import com.ruzz.butilordering.Model.DeliveryPersonnel;
import com.ruzz.butilordering.Model.LocationModel;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.Model.UserModel;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.ViewModels.DeliverConfirmViewModel;
import com.ruzz.butilordering.ViewModels.DeliverConfirmViewModelFactory;
import com.ruzz.butilordering.databinding.ActivityDeliveryConfirmBinding;
import com.ruzz.butilordering.databinding.DeliveryPaymentDialogBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DeliveryConfirmActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityDeliveryConfirmBinding binding;
    private DeliverConfirmViewModel deliverConfirmViewModel;
    private FirebaseAuth appAuth;
    private FirebaseFirestore database;
    private GeoLocation listener;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDeliveryConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        deliverConfirmViewModel = new ViewModelProvider(this, new DeliverConfirmViewModelFactory()).get(DeliverConfirmViewModel.class);
        appAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_delivery_confirm);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("Order");

        database.collection("orders").document(orderId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                OrderModel order = document.toObject(OrderModel.class);
                                order.setUserid(document.getId());
                                deliverConfirmViewModel.setCurrentOrder(order);
                            } else {
                                Toast.makeText(DeliveryConfirmActivity.this, "Order does not exist." + orderId,
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DeliveryConfirmActivity.this, "Failed to fetch order.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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

                    deliverConfirmViewModel.setCurrentUser(user);

                } else {
                    Toast.makeText(DeliveryConfirmActivity.this, "User does not exist.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DeliveryConfirmActivity.this, "Failed to fetch user.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.locationBottomDialog.setVisibility(View.GONE);

        binding.ibRecordLocation.setOnClickListener(v -> {
            Navigation.findNavController(this, R.id.nav_host_fragment_content_delivery_confirm).navigate(R.id.action_First2Fragment_to_Second2Fragment);
        });

        binding.buttonSubmitLocation.setOnClickListener(v -> {
            saveLocation();
        });

        binding.fabBack.setOnClickListener(v -> {
            goBackToDelivery();
        });

        fetchPersonnel(currentUser.getUid());
        getProducts();

        showLocationAlert();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_delivery_confirm);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setListener(GeoLocation listener) {
        this.listener = listener;
    }
    public void showLocationDialog() {
        binding.fabBack.setVisibility(View.GONE);
        binding.ibRecordLocation.setVisibility(View.GONE);
        binding.locationBottomDialog.setVisibility(View.VISIBLE);
        deliverConfirmViewModel.getOrderLocation().observe(this, location -> {
            binding.textAddress.setText(location.getAddress());
        });

        binding.buttonRefreshAddress.setOnClickListener(v -> {
            listener.getLocation();
        });
    }

    private void saveLocation() {
        OrderModel order = deliverConfirmViewModel.getCurrentOrder().getValue();
        LocationModel location = deliverConfirmViewModel.getOrderLocation().getValue();

        if (location != null) {
            database.collection("orders").document(order.getUserid())
                    .update("latitude", location.getLatitude());

            database.collection("orders").document(order.getUserid())
                    .update("longitude", location.getLongitude());

            Navigation.findNavController(this, R.id.nav_host_fragment_content_delivery_confirm).navigate(R.id.action_Second2Fragment_to_First2Fragment);
        } else {
            listener.getLocation();
        }
    }

    public void hideLocationDialog() {
        binding.locationBottomDialog.setVisibility(View.GONE);
        binding.ibRecordLocation.setVisibility(View.VISIBLE);
        binding.fabBack.setVisibility(View.VISIBLE);
    }

    private void fetchPersonnel(String uid) {
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
                                    deliverConfirmViewModel.setPersonnel(personnel);
                                }
                            } else {
                                Toast.makeText(DeliveryConfirmActivity.this, "Failed to fetch personnel.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void getProducts() {
        database.collection("products").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<ProductModel> productList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ProductModel currentProduct = new ProductModel(document.getId(), document.getString("name"), document.getString("image"),
                                    document.getDouble("stocks"), document.getDouble("storageLife"), document.getString("type"),
                                    document.getString("description"), document.getString("categoryId"), document.getDouble("content"), document.getDouble("price"),
                                    document.getDouble("promo"));
                            productList.add(currentProduct);
                        }

                        deliverConfirmViewModel.setProducts(productList);
                    } else {
                        Toast.makeText(DeliveryConfirmActivity.this, "Failed to fetch products.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void setDelivered() {
        OrderModel order = deliverConfirmViewModel.getCurrentOrder().getValue();

        if (order.isPaid()) {
            database.collection("orders").document(order.getUserid())
                    .update("delivered", true);

            DeliveryPersonnel personnel = deliverConfirmViewModel.getDeliveryPersonnel().getValue();

            database.collection("delivery").document(deliverConfirmViewModel.getCurrentUser().getValue().getUid())
                    .update("assignedDeliver", FieldValue.delete());

            List<OrderModel> deliveries = new ArrayList<>();

            for (OrderModel deliver : personnel.getAssignedDeliver()) {
                if (!deliver.getUserid().equals(order.getUserid())) {
                    deliveries.add(deliver);
                }
            }

            database.collection("delivery").document(deliverConfirmViewModel.getCurrentUser().getValue().getUid())
                    .update("assignedDeliver", deliveries);

            goBackToDelivery();
        } else {
            showBottomSheetDialog(order);
        }
    }

    private void showBottomSheetDialog(OrderModel order) {
        DeliveryPaymentDialogBinding paymentDialog = DeliveryPaymentDialogBinding.inflate(getLayoutInflater());
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(paymentDialog.getRoot());

        paymentDialog.textPayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Double change = Double.parseDouble(s.toString());
                deliverConfirmViewModel.setChange(change);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        deliverConfirmViewModel.getChange().observe(this, change -> {
            String changeText = "₱" + df.format(change);
            paymentDialog.textChange.setText(changeText);
        });

        paymentDialog.buttonSubmitPayment.setOnClickListener(v -> {
            double paymentAmount = Double.parseDouble(paymentDialog.textPayment.getText().toString());
            if (order.getAmountDue() < paymentAmount) {
                database.collection("orders").document(order.getUserid())
                        .update("delivered", true);

                database.collection("orders").document(order.getUserid())
                        .update("paid", true);

                database.collection("orders").document(order.getUserid())
                        .update("paymentProof", deliverConfirmViewModel.getCurrentUser().getValue().getUid());

                DeliveryPersonnel personnel = deliverConfirmViewModel.getDeliveryPersonnel().getValue();

                database.collection("delivery").document(deliverConfirmViewModel.getCurrentUser().getValue().getUid())
                        .update("assignedDeliver", FieldValue.delete());

                List<OrderModel> deliveries = new ArrayList<>();

                for (OrderModel deliver : personnel.getAssignedDeliver()) {
                    if (!deliver.getUserid().equals(order.getUserid())) {
                        deliveries.add(deliver);
                    }
                }

                database.collection("delivery").document(deliverConfirmViewModel.getCurrentUser().getValue().getUid())
                        .update("assignedDeliver", deliveries);

                goBackToDelivery();
            } else {
                Toast.makeText(DeliveryConfirmActivity.this, "Insufficient payment.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.show();
    }

    private void goBackToDelivery() {
        Intent intent = new Intent(this, DeliverActivity.class);
        startActivity(intent);
    }

    public void showLocationAlert() {
        AlertDialog.Builder gpsAlert = new AlertDialog.Builder(this);

        gpsAlert.setMessage(R.string.order_location_reminder).setTitle(R.string.location_reminder_title);

        gpsAlert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Navigation.findNavController(DeliveryConfirmActivity.this, R.id.nav_host_fragment_content_delivery_confirm).navigate(R.id.action_First2Fragment_to_Second2Fragment);
            }
        });

        gpsAlert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        gpsAlert.create();
        gpsAlert.show();
    }
}