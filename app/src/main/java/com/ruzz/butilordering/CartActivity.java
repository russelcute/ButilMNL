package com.ruzz.butilordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ruzz.butilordering.Adapter.CartAdapter;
import com.ruzz.butilordering.Adapter.OrderAdapter;
import com.ruzz.butilordering.Model.CartModel;
import com.ruzz.butilordering.Model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rvMyCart;
    private FloatingActionButton backPress;
    private TextView appbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        backPress=findViewById(R.id.fab_backPress);
        appbarTitle=findViewById(R.id.txt_appbarTitle);

        appbarTitle.setText("My Cart");
        backPress.setOnClickListener(view -> CartActivity.super.onBackPressed());

        List<CartModel> cartModelList = new ArrayList<>();
        cartModelList.add(new CartModel(R.drawable.gravy,"Almond","Spices","10","50","54","Good Product"));
        cartModelList.add(new CartModel(R.drawable.allspice,"AllSpice","Spices","10","50","54","Good Product"));
        cartModelList.add(new CartModel(R.drawable.fivespice,"FiveSpice","Spices","10","50","54","Good Product"));
        cartModelList.add(new CartModel(R.drawable.walnut,"Walnut","Spices","10","50","54","Good Product"));

        rvMyCart = findViewById(R.id.rv_cart);
        CartAdapter cartAdapter = new CartAdapter(this,cartModelList);
        rvMyCart.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1,RecyclerView.VERTICAL, false);
        rvMyCart.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rvMyCart.setLayoutManager(layoutManager);
        rvMyCart.setItemAnimator(new DefaultItemAnimator());
        rvMyCart.setAdapter(cartAdapter);

    }
}