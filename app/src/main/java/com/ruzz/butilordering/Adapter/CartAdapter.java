package com.ruzz.butilordering.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruzz.butilordering.Model.CartModel;
import com.ruzz.butilordering.Model.CategoryModel;
import com.ruzz.butilordering.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewHolder> {
    private Context ctx;
    private List<CartModel> cartModels;

    public CartAdapter(Context ctx, List<CartModel> cartModels) {
        this.ctx = ctx;
        this.cartModels = cartModels;
    }

    @NonNull
    @Override
    public CartAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(ctx).inflate(R.layout.item_cart,parent, false);
        return new CartAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewHolder holder, int position) {
        final CartModel cartModel=cartModels.get(position);
        holder.cartTitle.setText(cartModel.getTitle());
        holder.cartBrand.setText(cartModel.getBrand());
        holder.cartQuanity.setText("Quantity: "+cartModel.getQuantity());
        holder.cartOriginalPrice.setText("P "+cartModel.getOriginalPrice());
        holder.cartDiscountedPrice.setText("P "+cartModel.getDiscountedPrice());
        holder.cartDescription.setText(cartModel.getDescription());
        Glide.with(ctx).load(cartModel.getCartImage()).into(holder.cartImage);
    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView cartImage;
        private TextView cartTitle,cartBrand,cartQuanity,cartOriginalPrice,cartDiscountedPrice,cartDescription;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.iv_cartImage);
            cartTitle = itemView.findViewById(R.id.txt_cartTitle);
            cartBrand = itemView.findViewById(R.id.txt_cartBrand);
            cartQuanity = itemView.findViewById(R.id.txt_cartQuanity);
            cartOriginalPrice = itemView.findViewById(R.id.txt_cartOriginalPrice);
            cartDiscountedPrice = itemView.findViewById(R.id.txt_cartDiscountedPrice);
            cartDescription = itemView.findViewById(R.id.txt_cartDescription);
        }
    }
}
