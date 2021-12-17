package com.ruzz.butilordering;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrackingActivity extends AppCompatActivity {
    private FloatingActionButton backPress;
    private TextView appbarTitle;
    private ImageView rvTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        rvTracker=findViewById(R.id.rv_myTracker);
        backPress=findViewById(R.id.fab_backPress);
        appbarTitle=findViewById(R.id.txt_appbarTitle);

        appbarTitle.setText("Tracking");
        backPress.setOnClickListener(view -> TrackingActivity.super.onBackPressed());

    }
}