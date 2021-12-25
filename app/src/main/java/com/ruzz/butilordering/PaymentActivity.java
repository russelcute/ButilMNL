package com.ruzz.butilordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ruzz.butilordering.HomeFragments.OffersFragment;
import com.ruzz.butilordering.Model.LocationModel;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.PaymentFragments.OrderInfoFragment;
import com.ruzz.butilordering.PaymentFragments.OrderLocationFragment;
import com.ruzz.butilordering.ViewModels.PaymentViewModel;
import com.ruzz.butilordering.ViewModels.PaymentViewModelFactory;
import com.ruzz.butilordering.databinding.ActivityPaymentBinding;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    private ActivityPaymentBinding binding;
    private PaymentViewModel paymentViewModel;
    private FirebaseFirestore database;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        paymentViewModel = new ViewModelProvider(this, new PaymentViewModelFactory()).get(PaymentViewModel.class);
        database = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("Order");
        String requestPage = intent.getStringExtra("Page");

        if (orderId == null) {
            gotoHomePage();
        }

        paymentViewModel.setCurrentPage(requestPage);
        binding.txtCheckoutTitle.setText(orderId);

        database.collection("products").orderBy("promo", Query.Direction.DESCENDING).get()
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

                        paymentViewModel.setProducts(productList);
                    } else {
                        Toast.makeText(PaymentActivity.this, "Failed to fetch products.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        database.collection("orders").document(orderId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            OrderModel userOrder = document.toObject(OrderModel.class);
                            paymentViewModel.setCurrentOrder(userOrder);
                        } else {
                            gotoHomePage();
                        }
                    }
                });

        binding.fabBackPress.setOnClickListener(v -> {
            String page = paymentViewModel.getCurrentPage().getValue();
            if ("OrderLocation".equals(page)) {
               paymentViewModel.setCurrentPage("OrderInfo");
            } else {
                gotoHomePage();
            }
        });

        binding.ibOrderLocate.setOnClickListener(v -> {
            OrderModel curentOrder = paymentViewModel.getCurrentOrder().getValue();
            if (curentOrder.getLatitude() != 0 && curentOrder.getLongitude() != 0) {
                paymentViewModel.setCurrentPage("OrderLocation");
            } else {
                showErrorDialog(R.string.order_location_error, R.string.error_order_title);
            }
        });

        binding.ibOrderCancel.setOnClickListener(v -> {
            deleteOrder(orderId);
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        paymentViewModel.getCurrentPage().observe(this, page -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if ("OrderLocation".equals(page)) {

                transaction.setReorderingAllowed(true);
                transaction.replace(binding.fragmentContainerOrder.getId(), OrderLocationFragment.class, null);
                transaction.commit();

                binding.ibOrderCancel.setVisibility(View.GONE);
                binding.ibOrderLocate.setVisibility(View.GONE);
                binding.locationBottomDialog.setVisibility(View.VISIBLE);
                binding.checkOutDialog.setVisibility(View.GONE);
            } else {

                transaction.setReorderingAllowed(true);
                transaction.replace(binding.fragmentContainerOrder.getId(), OrderInfoFragment.class, null);
                transaction.commit();

                binding.ibOrderCancel.setVisibility(View.VISIBLE);
                binding.ibOrderLocate.setVisibility(View.VISIBLE);
                binding.locationBottomDialog.setVisibility(View.GONE);
                binding.checkOutDialog.setVisibility(View.VISIBLE);
            }
        });

        paymentViewModel.getCurrentOrder().observe(this, order -> {
            String amount = "Amount Due:" + " " + "₱" + df.format(order.getAmountDue());
            binding.txtAmountDue.setText(amount);

            if (order.getLatitude() != 0 && order.getLongitude() != 0) {
                Geocoder geocoder = new Geocoder(this.getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(order.getLatitude(), order.getLongitude(), 1);
                    String address = addresses.get(0).getAddressLine(0);

                    binding.textAddress.setText(address);

                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteOrder(String id) {
        database.collection("orders").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        gotoHomePage();
                    }
                });
    }


    private void gotoHomePage() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("Page", "Orders");
        startActivity(intent);
    }

    private void showErrorDialog(int message, int title) {
        AlertDialog.Builder gpsAlert = new AlertDialog.Builder(this);

        gpsAlert.setMessage(message).setTitle(title);

        gpsAlert.setPositiveButton(R.string.ok, (dialog, which) -> {

        });
        gpsAlert.create();
        gpsAlert.show();
    }
}