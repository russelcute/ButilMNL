package com.ruzz.butilordering.Repository;

import static android.content.ContentValues.TAG;

import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ruzz.butilordering.Model.DealsModel;
import com.ruzz.butilordering.Model.OfferModel;

import java.util.ArrayList;
import java.util.List;

public class DealsRepository {
    final FirebaseFirestore database = FirebaseFirestore.getInstance();

    public List<DealsModel> getDealsFromFirebase() {
        List<DealsModel> deals = new ArrayList<>();

        database.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String image = document.getString("cartImage");
                                String title = document.getString("title");
                                String type = document.getString("type");
                                String discountedPrice = "P" + document.getDouble("discountedPrice");
                                String originalPrice = "P" + document.getDouble("originalPrice");
                                String storageLife = document.getDouble("storageLife") + " days";
                                String discountPercentage = "-" + document.getDouble("discountPercentage") + "%";

                                Log.v(TAG, discountedPrice);
                                Log.v(TAG, "Fetching data...");

                                DealsModel currentDeal = new DealsModel(image, title, type, storageLife, discountedPrice, originalPrice, discountPercentage);
                                deals.add(currentDeal);
                            }
                        }
                    }
                });

        Log.e("Message", "Fetched Data");
        Log.e("Message", deals.get(0).dealsTitle);

        return deals;
    }

}
