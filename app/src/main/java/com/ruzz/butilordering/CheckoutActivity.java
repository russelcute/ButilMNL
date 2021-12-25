package com.ruzz.butilordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ruzz.butilordering.Adapter.GeoLocation;
import com.ruzz.butilordering.CheckoutFragments.CheckoutFragment;
import com.ruzz.butilordering.CheckoutFragments.UserAddressFragment;
import com.ruzz.butilordering.Model.CartModel;
import com.ruzz.butilordering.Model.LocationModel;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.Model.UserModel;
import com.ruzz.butilordering.ViewModels.CheckoutViewModel;
import com.ruzz.butilordering.ViewModels.CheckoutViewModelFactory;
import com.ruzz.butilordering.databinding.ActivityCheckoutBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private ActivityCheckoutBinding binding;
    private FirebaseAuth appAuth;
    private CheckoutViewModel checkoutViewModel;
    private FirebaseFirestore database;
    private GeoLocation listener;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        checkoutViewModel = new ViewModelProvider(this, new CheckoutViewModelFactory()).get(CheckoutViewModel.class);
        getProducts();

        binding.buttonPlaceOrder.setOnClickListener(v -> placeOrder());

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = appAuth.getCurrentUser();

        if (currentUser == null) {
            goToLoginActivity();
        }

        getCartItems(currentUser.getUid());

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

                    checkoutViewModel.setCurrentUser(user);

                } else {
                    Toast.makeText(CheckoutActivity.this, "User does not exist.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CheckoutActivity.this, "Failed to fetch user.",
                        Toast.LENGTH_SHORT).show();
            }
        });


        checkoutViewModel.getUserOrder().observe(this, order -> {
            String price = "Total Price: P" + df.format(order.getAmountDue());
            binding.txtPlacePrice.setText(price);
        });

        checkoutViewModel.getCurrentPage().observe(this, page -> {
            switch (page) {
                case "PlaceOrder":
                    switchToOrderFragment();

                    binding.fabBackPress.setOnClickListener(v -> gotoCartPage());
                    break;
                case "Location":
                    switchToMapFragment();

                    binding.fabBackPress.setOnClickListener(v -> {
                        switchToOrderFragment();
                        checkoutViewModel.setCurerntPage("PlaceOrder");
                    });
                    break;
                default:
                    //none
            }
        });

        checkoutViewModel.getUserLocation().observe(this, location -> binding.textAddress.setText(location.getAddress()));

    }

    private void getCartItems(String user) {
        database.collection("carts").document(user)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    CartModel userCart = documentSnapshot.toObject(CartModel.class);
                    if (userCart != null && userCart.getItems() != null) {
                        OrderModel order = new OrderModel(userCart.getItems());
                        checkoutViewModel.setUserOrder(order);
                    }
                });
    }

    public void setListener(GeoLocation listener) {
        this.listener = listener;
    }

    private void getProducts() {
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

                        checkoutViewModel.setProducts(productList);
                    } else {
                        Toast.makeText(CheckoutActivity.this, "Failed to fetch products.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void switchToMapFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        // transaction.addToBackStack("products");
        transaction.replace(binding.fragmentContainerCheckout.getId(), UserAddressFragment.class, null);
        transaction.commit();
        binding.userLocationDialog.setVisibility(View.VISIBLE);
        binding.checkOutDialog.setVisibility(View.GONE);

        binding.buttonRefreshAddress.setOnClickListener(v -> listener.getLocation());

        binding.buttonSubmitAddress.setOnClickListener(v -> checkoutViewModel.setCurerntPage("PlaceOrder"));


    }

    public void switchToOrderFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        // transaction.addToBackStack("products");
        transaction.replace(binding.fragmentContainerCheckout.getId(), CheckoutFragment.class, null);
        transaction.commit();
        binding.userLocationDialog.setVisibility(View.GONE);
        binding.checkOutDialog.setVisibility(View.VISIBLE);
    }

    private void gotoCartPage() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("Page", "Cart");
        startActivity(intent);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void placeOrder() {
        OrderModel userOrder = checkoutViewModel.getUserOrder().getValue();
        String paymentType = checkoutViewModel.getPaymentType().getValue();
        LocationModel userLocation = checkoutViewModel.getUserLocation().getValue();

        if (userOrder != null && userLocation != null) {
            UserModel currentUser = checkoutViewModel.getCurrentUser().getValue();
            currentUser.setAddress(userLocation.getAddress());
            currentUser.setLatitudeLongitude(userLocation.getLatitude(), userLocation.getLongitude());

            userOrder.setCustomer(currentUser);
            userOrder.setUserid(currentUser.getUid());
            userOrder.setPaymentType(paymentType);
            userOrder.setCoordinates(0, 0);

            database.collection("orders")
                    .add(userOrder)
                    .addOnFailureListener(e -> showErrorDialog(R.string.order_error_submit, R.string.order_submit_error_title));

            database.collection("carts").document(currentUser.getUid())
                    .update("items", FieldValue.delete());

            database.collection("carts").document(currentUser.getUid())
                    .update("totalPrice", 0);

            gotoCartPage();

        } else {
            showErrorDialog(R.string.order_error_message, R.string.order_error_title);
        }
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