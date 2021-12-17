package com.ruzz.butilordering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ruzz.butilordering.Adapter.CategoryAdapter;
import com.ruzz.butilordering.Adapter.OrderAdapter;
import com.ruzz.butilordering.Model.CategoryModel;
import com.ruzz.butilordering.Model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends AppCompatActivity {
    private RecyclerView rvMyOrder;
    private FloatingActionButton backPress;
    private TextView appbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        rvMyOrder=findViewById(R.id.rv_myOrder);

        backPress=findViewById(R.id.fab_backPress);
        appbarTitle=findViewById(R.id.txt_appbarTitle);

        appbarTitle.setText("My Orders");
        backPress.setOnClickListener(view -> MyOrderActivity.super.onBackPressed());


        List<OrderModel> orderModelList = new ArrayList<>();
        orderModelList.add(new OrderModel(R.drawable.almond,"0","2021-12-13","Almond"));
        orderModelList.add(new OrderModel(R.drawable.walnut,"1","2021-12-13","Walnut"));
        orderModelList.add(new OrderModel(R.drawable.gravy,"2","2021-12-13","Gravy"));
        orderModelList.add(new OrderModel(R.drawable.allspice,"1","2021-12-13","AllSpice"));
        orderModelList.add(new OrderModel(R.drawable.fivespice,"0","2021-12-13","FiveSpice"));

        OrderAdapter orderAdapter = new OrderAdapter(this,orderModelList);
        rvMyOrder.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1,RecyclerView.VERTICAL, false);
        rvMyOrder.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rvMyOrder.setLayoutManager(layoutManager);
        rvMyOrder.setItemAnimator(new DefaultItemAnimator());
        rvMyOrder.setAdapter(orderAdapter);
    }
}