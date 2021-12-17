package com.ruzz.butilordering.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruzz.butilordering.Model.OfferModel;
import com.ruzz.butilordering.R;

import java.util.ArrayList;
import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.viewHolder>{
    private Context ctx;
    private List<OfferModel> offerModelList = new ArrayList<>();

    public OfferAdapter(Context ctx, List<OfferModel> offerModelList) {
        this.ctx = ctx;
        this.offerModelList = offerModelList;
    }
    @NonNull
    @Override
    public OfferAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(ctx).inflate(R.layout.item_offers,viewGroup, false);
        return new OfferAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.viewHolder viewHolder, int i) {
        final OfferModel offerModel=offerModelList.get(i);
        viewHolder.offerPrice.setText(offerModel.getOfferPrice());
        viewHolder.offerConstraint.setText(offerModel.getOfferConstraint());
        Glide.with(ctx).load(offerModel.getOfferImage()).into(viewHolder.offerImage);
    }

    @Override
    public int getItemCount() {
        return offerModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView offerImage;
        private TextView offerPrice, offerConstraint;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            offerImage=itemView.findViewById(R.id.iv_offerImage);
            offerPrice=itemView.findViewById(R.id.txt_offerPrice);
            offerConstraint=itemView.findViewById(R.id.txt_offerConstraint);
        }
    }
}
