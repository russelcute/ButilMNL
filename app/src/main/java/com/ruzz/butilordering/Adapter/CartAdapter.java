package com.ruzz.butilordering.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruzz.butilordering.HomeActivity;
import com.ruzz.butilordering.Model.ProductCartModel;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.databinding.CartItemBinding;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<ProductCartModel> cartItems;
    private CartItemSelected listener;
    private boolean hideDelete;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public CartAdapter(CartItemSelected listener, List<ProductCartModel> items) {
        this.listener = listener;
        this.cartItems = items;
        this.hideDelete = false;
    }

    public void setHideDelete() {
        hideDelete = true;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductCartModel item = cartItems.get(position);
        ProductModel currentProduct = listener.searchProduct(item.getProductId());

        if (currentProduct == null) {
            holder.itemBinding.txtCartTitle.setText("Content not found.");
            holder.itemBinding.txtCartBrand.setText("Content not found.");
            holder.itemBinding.txtCartQuanity.setText("Content not found.");
            holder.itemBinding.txtCartPrice.setText("Content not found.");
        } else {

            ProductCartModel currentItem = new ProductCartModel(item.getProductId(), item.getQuantity(), currentProduct.getPrice(), currentProduct.getPromo());

            holder.itemBinding.txtCartTitle.setText(currentProduct.getName());
            String type = currentProduct.getType() + " " + currentProduct.getCategoryId();
            holder.itemBinding.txtCartBrand.setText(type);
            Picasso.get().load(currentProduct.getImage()).into(holder.itemBinding.ivCartImage);
            String quantity = "Quantity:" + " " + currentItem.getQuantity();
            holder.itemBinding.txtCartQuanity.setText(quantity);
            String price = "₱" + df.format(currentItem.getPrice());
            holder.itemBinding.txtCartPrice.setText(price);

            if (hideDelete) {
                holder.itemBinding.ibDelete.setVisibility(View.INVISIBLE);
            }

            holder.itemBinding.txtCartQuanity.setOnClickListener(v -> {
                listener.modifyItem(position, currentProduct);
            });

            holder.itemBinding.ibDelete.setOnClickListener(v -> {
                listener.removeItem(position, currentProduct);
            });

            holder.itemBinding.ivCartImage.setOnClickListener(v -> {
                listener.itemSelected(currentProduct);
            });

            holder.itemBinding.txtCartTitle.setOnClickListener(v -> {
                listener.itemSelected(currentProduct);
            });
        }
    }

    @Override
    public int getItemCount() {
       if (this.cartItems != null) {
            return this.cartItems.size();
       } else {
            return 0;
       }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CartItemBinding itemBinding;

        public ViewHolder(CartItemBinding view) {
            super(view.getRoot());
            itemBinding = view;
        }
    }
}
