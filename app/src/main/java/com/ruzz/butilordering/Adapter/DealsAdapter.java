package com.ruzz.butilordering.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruzz.butilordering.Model.DealsModel;
import com.ruzz.butilordering.Model.OfferModel;
import com.ruzz.butilordering.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.viewHolder> {
    private Context ctx;
    private List<DealsModel> dealsModelList;

    public DealsAdapter(Context ctx, List<DealsModel> dealsModelList) {
        this.ctx = ctx;
        this.dealsModelList = dealsModelList;
    }

    @NonNull
    @Override
    public DealsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(ctx).inflate(R.layout.item_deals,parent, false);
        return new DealsAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealsAdapter.viewHolder holder, int position) {
        final DealsModel dealsModel=dealsModelList.get(position);
        holder.dealsTitle.setText(dealsModel.getDealsTitle());
        holder.dealsType.setText(dealsModel.getDealsType());
        holder.dealsStorageLife.setText(dealsModel.getDealsStorageLife());
        holder.dealsOriginalPrice.setText(dealsModel.getDealsOriginalPrice());
        holder.dealsDiscountedPrice.setText(dealsModel.getDealsDiscountedPrice());
        holder.dealsDiscountedPercentage.setText(dealsModel.getDealsDiscountedPercentage());
        Drawable productImage = LoadImageFromWebOperations(dealsModel.getDealsImage());
        Glide.with(ctx).load(productImage).into(holder.dealsImage);
    }

    private static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "image");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return dealsModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView dealsImage;
        private TextView dealsTitle,dealsType,dealsStorageLife,dealsOriginalPrice,dealsDiscountedPrice,dealsDiscountedPercentage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dealsImage = itemView.findViewById(R.id.rv_dealsImage);
            dealsTitle = itemView.findViewById(R.id.txt_dealsTitle);
            dealsType = itemView.findViewById(R.id.txt_dealsType);
            dealsStorageLife = itemView.findViewById(R.id.txt_dealsLife);
            dealsOriginalPrice = itemView.findViewById(R.id.txt_dealsOriginalPrice);
            dealsDiscountedPrice = itemView.findViewById(R.id.txt_dealsDiscountedPrice);
            dealsDiscountedPercentage = itemView.findViewById(R.id.txt_dealsDiscountedPercentage);

        }
    }
}
