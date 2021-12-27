package com.ruzz.butilordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruzz.butilordering.HomeFragments.CartFragment;
import com.ruzz.butilordering.HomeFragments.OffersFragment;
import com.ruzz.butilordering.HomeFragments.OrderFragment;
import com.ruzz.butilordering.Model.AccountModel;
import com.ruzz.butilordering.Model.CartModel;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.Model.ProductCartModel;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.Model.UserModel;
import com.ruzz.butilordering.ViewModels.HomeViewModel;
import com.ruzz.butilordering.ViewModels.HomeViewModelFactory;
import com.ruzz.butilordering.databinding.ActivityHomeBinding;
import com.ruzz.butilordering.databinding.CartBottomSheetBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "Error";
    private FirebaseAuth appAuth;
    private FirebaseFirestore database;
    private FragmentManager fragmentManager;
    private HomeViewModel homeViewModel;
    private DrawerLayout drawer;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.ruzz.butilordering.databinding.ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory()).get(HomeViewModel.class);

        drawer = binding.drawerLayout;
        ImageView listMode = findViewById(R.id.list_Mode);
        listMode.setOnClickListener(v -> {
            if(drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            }
            else {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        Intent intent = getIntent();
        String pageRequest = intent.getStringExtra("Page");

        if (pageRequest != null) {
            homeViewModel.setCurrentPage(pageRequest);
        }

        fragmentManager = getSupportFragmentManager();
        NavigationView navigation = binding.navView;
        View headerView = navigation.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.customerName);
        TextView navEmail = headerView.findViewById(R.id.customerEmail);

        homeViewModel.getCurrentUser().observe(this, user -> {
           if (user.getUserName() != null && user.getEmail() != null) {
               navUsername.setText(user.getUserName());
               navEmail.setText(user.getEmail());
           }
        });

        homeViewModel.getCartTotalPrice().observe(this, price -> {
            TextView totalPriceView = findViewById(R.id.txt_totalPrice);
            String priceDisplay = "Total Price:" + " " + "₱" + df.format(price);
            totalPriceView.setText(priceDisplay);
        });

        homeViewModel.getCurrentPage().observe(this, page -> {
            switch (page) {
                case "Home":
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setReorderingAllowed(true);
                    transaction.replace(R.id.fragment_container_view, OffersFragment.class, null);
                    transaction.commit();
                    break;
                case "Cart":
                    FragmentTransaction transaction_1 = fragmentManager.beginTransaction();
                    transaction_1.setReorderingAllowed(true);
                    transaction_1.replace(R.id.fragment_container_view, CartFragment.class, null);
                    transaction_1.commit();
                    break;
                case "Orders":
                    FragmentTransaction transaction_2 = fragmentManager.beginTransaction();
                    transaction_2.setReorderingAllowed(true);
                    transaction_2.replace(R.id.fragment_container_view, OrderFragment.class, null);
                    transaction_2.commit();
                    break;
                default:
                    drawer.closeDrawer(GravityCompat.START);
            }
        });

        Button checkOut = findViewById(R.id.buttonCheckout);
        checkOut.setOnClickListener(v -> goToCheckoutActivity());

        setupDrawerContent(navigation);

    }

    private void setupDrawerContent(NavigationView nvDrawer) {
        nvDrawer.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });

    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                goToLoginActivity();
                break;
            case R.id.nav_home:
                homeViewModel.setCurrentPage("Home");
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_cart:
                homeViewModel.setCurrentPage("Cart");
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_order:
                homeViewModel.setCurrentPage("Orders");
                drawer.closeDrawer(GravityCompat.START);
                break;
            default:
                Toast.makeText(HomeActivity.this, "Not assigned yet.",
                        Toast.LENGTH_SHORT).show();
        }
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
                    UserModel user = new UserModel(currentUser.getUid(), username, gender, currentUser.getEmail());

                    homeViewModel.setCurrentUser(user);
                } else {
                    Toast.makeText(HomeActivity.this, "User does not exist.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(HomeActivity.this, "Failed to fetch user.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        getUserLikedProducts(currentUser.getUid());

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

                        homeViewModel.setProductList(productList);
                    } else {
                        Toast.makeText(HomeActivity.this, "Failed to fetch products.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        database.collection("orders").whereEqualTo("userid", currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<OrderModel> userOrders = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            OrderModel order = document.toObject(OrderModel.class);
                            order.setUserid(document.getId());
                            userOrders.add(order);
                        }

                        homeViewModel.setUserOrders(userOrders);
                    }
                });

        getCartItems(currentUser.getUid());

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        // transaction.addToBackStack("products");
        transaction.replace(R.id.fragment_container_view, OffersFragment.class, null);
        transaction.commit();
        hideCheckOut(true);

    }

    private void getCartItems(String user) {
        database.collection("carts").document(user)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    try {
                        CartModel userCart = documentSnapshot.toObject(CartModel.class);
                        homeViewModel.setUserCart(userCart);
                        List<ProductCartModel> items = new ArrayList<>(userCart.getItems());
                        double total = 0;
                        for (ProductCartModel item : items) {
                            total += item.getPrice();
                        }
                        updateTotalPrice(total, user);
                        homeViewModel.setCartTotalPrice(total);
                    } catch (NullPointerException e) {
                        Log.d(TAG, e.getMessage());
                    }
                });
    }

    private void getUserLikedProducts(String uid) {
        database.collection("accounts").document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                        AccountModel account = documentSnapshot.toObject(AccountModel.class);
                        if (account != null) {
                            homeViewModel.setLikedProducts(account.getLiked());
                        } else {
                            Toast.makeText(HomeActivity.this, "Failed to fetch liked.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateTotalPrice(double price, String user) {
        database.collection("carts").document(user)
                .update("totalPrice", price);
    }

    public void hideCheckOut(boolean value) {
        CardView checkOut = findViewById(R.id.checkOut);
        if (value) {
            checkOut.setVisibility(View.GONE);
        } else {
            checkOut.setVisibility(View.VISIBLE);
        }

    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToCheckoutActivity() {
        if (homeViewModel.getUserCart().getValue().getItems() != null) {
            Intent intent = new Intent(this, CheckoutActivity.class);
            startActivity(intent);
        }
    }

    public void gotoOrderActivity(String productId, String page) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("Page", page);
        intent.putExtra("Order", productId);
        startActivity(intent);
    }

    public void gotoProductProfile() {
        Intent intent = new Intent(this, ProductProfile.class);
        String productId = homeViewModel.getSelectedProduct().getValue().getUid();
        intent.putExtra("productId", productId);
        startActivity(intent);
    }

    public void gotoSelectedProduct(String productId) {
        Intent intent = new Intent(this, ProductProfile.class);
        intent.putExtra("productId", productId);
        startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showButtomSheetDialog() {
        CartBottomSheetBinding addCart = CartBottomSheetBinding.inflate(getLayoutInflater());
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(addCart.getRoot());

        Handler repeatUpdateHandler = new Handler();
        final boolean[] autoIncrement = {false};
        final boolean[] autoDecrement = {false};

        class RepetitiveUpdater implements Runnable {

            @Override
            public void run() {
                if (autoIncrement[0]) {
                    homeViewModel.addQuantity();
                    repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), 50);
                } else if (autoDecrement[0]) {
                    homeViewModel.minusQuantity();
                    repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), 50);
                }
            }

        }

        addCart.button.setText("Update Quantity");
        addCart.ibAdd.setOnClickListener(v -> homeViewModel.addQuantity());

        addCart.ibAdd.setOnLongClickListener(v -> {
            autoIncrement[0] = true;
            repeatUpdateHandler.post(new RepetitiveUpdater());
            return false;
        });

        addCart.ibAdd.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement[0]) {
                autoIncrement[0] = false;
            }
            return false;
        });

        addCart.ibRemove.setOnClickListener(v -> homeViewModel.minusQuantity());

        addCart.ibRemove.setOnLongClickListener(v -> {
            autoDecrement[0] = true;
            repeatUpdateHandler.post(new RepetitiveUpdater());
            return false;
        });

        addCart.ibRemove.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement[0]) {
                autoDecrement[0] = false;
            }
            return false;
        });

        addCart.button.setOnClickListener(v -> {
            updateItemQuantity();
            bottomSheetDialog.dismiss();
            getCartItems(homeViewModel.getCurrentUser().getValue().getUid());
        });

        homeViewModel.getQuantity().observe(this, num -> addCart.textQuantity.setText(Integer.toString(num)));

        bottomSheetDialog.show();
    }

    public void updateItemQuantity() {
        ProductCartModel item = homeViewModel.getSelectedItem().getValue();
        ProductModel product = homeViewModel.getSelectedProduct().getValue();

        ProductCartModel oldItem = new ProductCartModel(item.getProductId(), item.getQuantity(),
                product.getPrice(), product.getPromo());

        database.collection("carts").document(homeViewModel.getCurrentUser().getValue().getUid())
                .update("items", FieldValue.arrayRemove(oldItem));

        ProductCartModel newItem = new ProductCartModel(item.getProductId(), homeViewModel.getQuantity().getValue(),
                product.getPrice(), product.getPromo());

        database.collection("carts").document(homeViewModel.getCurrentUser().getValue().getUid())
                .update("items", FieldValue.arrayUnion(newItem));

    }

    public void removeItemCart() {
        ProductCartModel item = homeViewModel.getSelectedItem().getValue();
        ProductModel product = homeViewModel.getSelectedProduct().getValue();

        ProductCartModel oldItem = new ProductCartModel(item.getProductId(), item.getQuantity(),
                product.getPrice(), product.getPromo());

        database.collection("carts").document(homeViewModel.getCurrentUser().getValue().getUid())
                .update("items", FieldValue.arrayRemove(oldItem));

        getCartItems(homeViewModel.getCurrentUser().getValue().getUid());
    }
}