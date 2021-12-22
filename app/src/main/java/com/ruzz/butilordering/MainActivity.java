package com.ruzz.butilordering;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ruzz.butilordering.Adapter.CategoryAdapter;
import com.ruzz.butilordering.Adapter.DealsAdapter;
import com.ruzz.butilordering.Adapter.OfferAdapter;
import com.ruzz.butilordering.Adapter.SliderAdapter;
import com.ruzz.butilordering.Model.CategoryModel;
import com.ruzz.butilordering.Model.DealsModel;
import com.ruzz.butilordering.Model.OfferModel;
import com.ruzz.butilordering.Model.SliderModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView listMode;
    DrawerLayout drawer;
    RecyclerView rvOff,rvCategories,rvDeals;
    private final List<OfferModel> offerModelList=new ArrayList<>();
    SliderView sliderView;
    private FirebaseAuth appAuthentication;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listMode=findViewById(R.id.list_Mode);
        drawer=findViewById(R.id.drawer_layout);
        listMode.setOnClickListener(view -> drawer.openDrawer(Gravity.LEFT));

        appAuthentication = FirebaseAuth.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        //RecyclerView for offers starts here
        offerModelList.add(new OfferModel(R.drawable.part1,"P100 Cashback","first order above P500"));
        offerModelList.add(new OfferModel(R.drawable.part2,"P100 Cashback","first order above P500"));
        offerModelList.add(new OfferModel(R.drawable.part3,"P100 Cashback","first order above P500"));
        offerModelList.add(new OfferModel(R.drawable.part4,"P100 Cashback","first order above P500"));
        offerModelList.add(new OfferModel(R.drawable.part5,"P100 Cashback","first order above P500"));

        rvOff=findViewById(R.id.rv_offer);
        OfferAdapter offerAdapter=new OfferAdapter(this,offerModelList);
        rvOff.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this,1,RecyclerView.HORIZONTAL, false);
        rvOff.setLayoutManager(manager);
        rvOff.setItemAnimator(new DefaultItemAnimator());
        rvOff.setAdapter(offerAdapter);

        //image slider ito
        SliderModel[] sliderModels = new SliderModel[]{
                new SliderModel(R.drawable.image1),
                new SliderModel(R.drawable.image2),
                new SliderModel(R.drawable.image3),
        };

        sliderView = findViewById(R.id.imageSlider);
        SliderAdapter sliderAdapter = new SliderAdapter(sliderModels,this);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setAutoCycle(true);
        sliderView.setScrollTimeInSec(3);
        sliderView.startAutoCycle();

        //Category Products start here
        List<CategoryModel> categoryModelList = new ArrayList<>();
        categoryModelList.add(new CategoryModel(R.drawable.herb,"Herbs"));
        categoryModelList.add(new CategoryModel(R.drawable.spices,"Spices"));

        rvCategories=findViewById(R.id.rv_categories);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this,categoryModelList);
        rvCategories.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1,RecyclerView.HORIZONTAL, false);
        rvCategories.setLayoutManager(layoutManager);
        rvCategories.setItemAnimator(new DefaultItemAnimator());
        rvCategories.setAdapter(categoryAdapter);

        NavigationView nvDrawer = findViewById(R.id.nav_view);
        setupDrawerContent(nvDrawer);

    }

    private void setupDrawerContent(NavigationView nvDrawer) {
        nvDrawer.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });

    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                goToLoginActivity();
                break;
            case R.id.nav_about:
                Toast.makeText(MainActivity.this, "Clicked.",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_help:
                Toast.makeText(MainActivity.this, "Clicked.",
                        Toast.LENGTH_SHORT).show();
            default:
                Toast.makeText(MainActivity.this, "Default.",
                        Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = appAuthentication.getCurrentUser();

        if (currentUser == null) {
            goToLoginActivity();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(Gravity.LEFT);
        }else {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Confirm Exit?");
            adb.setMessage("Are you sure you want to exit?");
            adb.setCancelable(false);

            adb.setPositiveButton("Yes", (dialogInterface, i) -> finish());
            adb.setNegativeButton("No", null);

            AlertDialog alertDialog=adb.create();
            alertDialog.show();
        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}