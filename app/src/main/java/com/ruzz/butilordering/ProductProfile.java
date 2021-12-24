package com.ruzz.butilordering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ruzz.butilordering.HomeFragments.ProductProfileFragment;
import com.ruzz.butilordering.Model.CartModel;
import com.ruzz.butilordering.Model.ProductCartModel;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.ViewModels.ProfileViewModel;
import com.ruzz.butilordering.ViewModels.ProfileViewModelFactory;
import com.ruzz.butilordering.databinding.ActivityProductProfileBinding;
import com.ruzz.butilordering.databinding.CartBottomSheetBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductProfile extends AppCompatActivity {
    private FirebaseFirestore database;
    private ProfileViewModel productViewModel;
    private FirebaseAuth appAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.ruzz.butilordering.databinding.ActivityProductProfileBinding binding = ActivityProductProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();
        appAuth = FirebaseAuth.getInstance();
        productViewModel = new ViewModelProvider(this, new ProfileViewModelFactory()).get(ProfileViewModel.class);

        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");

        binding.fabBackPress.setOnClickListener(v -> onBackPressed());

        database.collection("products").document(productId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ProductModel currentProduct = new ProductModel(document.getId(), document.getString("name"), document.getString("image"),
                                    document.getDouble("stocks"), document.getDouble("storageLife"), document.getString("type"),
                                    document.getString("description"), document.getString("categoryId"), document.getDouble("content"), document.getDouble("price"),
                                    document.getDouble("promo"));
                            productViewModel.setSelectedProduct(currentProduct);
                        } else {
                            Toast.makeText(ProductProfile.this, "Product not found.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        productViewModel.getSelectedProduct().observe(this, product -> {
            if (product != null) {
                setFeaturedProducts(product);
                binding.txtAppbarTitle.setText(product.getName());
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
        productViewModel.setUserId(currentUser.getUid());
        getCartItems(currentUser.getUid());
        gotoProductProfile();
    }

    public void setFeaturedProducts(ProductModel currentProduct) {
        if (currentProduct != null) {
            database.collection("products").whereEqualTo("categoryId", currentProduct.getCategoryId())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            List<ProductModel> productList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!currentProduct.getUid().equals(document.getId())) {
                                    ProductModel currentProduct1 = new ProductModel(document.getId(), document.getString("name"), document.getString("image"),
                                            document.getDouble("stocks"), document.getDouble("storageLife"), document.getString("type"),
                                            document.getString("description"), document.getString("categoryId"), document.getDouble("content"), document.getDouble("price"),
                                            document.getDouble("promo"));
                                    productList.add(currentProduct1);
                                }
                            }
                            productViewModel.setFeaturedProducts(productList);
                        } else {
                            Toast.makeText(ProductProfile.this, "Failed to fetch products.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void getCartItems(String user) {
        database.collection("carts").document(user)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                       try {
                           CartModel userCart = documentSnapshot.toObject(CartModel.class);
                           productViewModel.setUserCart(userCart);
                           productViewModel.setHasCart(true);
                       } catch (NullPointerException e) {

                       }
                    }
                });
    }


    public void gotoProductProfile() {
        productViewModel.resetQuantity();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragment_container, ProductProfileFragment.class, null);
        transaction.commit();
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
                    productViewModel.addQuantity();
                    repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), 50);
                } else if (autoDecrement[0]) {
                    productViewModel.minusQuantity();
                    repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), 50);
                }
            }

        }

        addCart.button.setText("Add to Cart");
        addCart.ibAdd.setOnClickListener(v -> productViewModel.addQuantity());

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

        addCart.ibRemove.setOnClickListener(v -> productViewModel.minusQuantity());

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
            addTocart();
            bottomSheetDialog.dismiss();
        });

        productViewModel.getQuantity().observe(this, num -> addCart.textQuantity.setText(Integer.toString(num)));

        bottomSheetDialog.show();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void addTocart() {
        boolean hasCart = productViewModel.getHasCart();
        ProductModel currProduct = productViewModel.getSelectedProduct().getValue();

        ProductCartModel cartItem = new ProductCartModel(currProduct.getUid(), productViewModel.getQuantity().getValue(), currProduct.getPrice(), currProduct.getPromo());
        double updatedTotal = productViewModel.getCurrentTotal() + cartItem.getPrice();

        if (hasCart) {
            database.collection("carts").document(productViewModel.getUserId())
                    .update("items", FieldValue.arrayUnion(cartItem));
            database.collection("carts").document(productViewModel.getUserId())
                    .update("totalPrice", updatedTotal);
        } else {
            List<ProductCartModel> productItems = new ArrayList<>();
            productItems.add(cartItem);

            CartModel addedCart = new CartModel(productItems);
            database.collection("carts").document(productViewModel.getUserId())
                    .set(addedCart);

        }
        productViewModel.setCurrentTotal(updatedTotal);
    }

}