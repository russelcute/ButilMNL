package com.ruzz.butilordering.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruzz.butilordering.HomeActivity;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.databinding.ProductItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<ProductModel> productList;
    private ProductSelected listener;

    public ProductsAdapter(ProductSelected listener, List<ProductModel> products) {
        this.productList = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        ProductModel currentProduct = productList.get(position);

        holder.itemBinding.txtDealsTitle.setText(currentProduct.getName());
        String type = currentProduct.getType() + " " + currentProduct.getCategoryId();
        holder.itemBinding.txtDealsType.setText(type);
        String price = "₱" + currentProduct.getPrice();
        holder.itemBinding.txtDealsPrice.setText(price);
        ImageView productImage = holder.itemBinding.rvDealsImage;
        Picasso.get().load(currentProduct.getImage()).into(productImage);

        if (currentProduct.getPromo() == 0) {
            holder.itemBinding.txtDealsDiscountedPercentage.setVisibility(View.GONE);
        } else {
            String promo = "—" + (int) currentProduct.getPromo() + "%";
            holder.itemBinding.txtDealsDiscountedPercentage.setText(promo);
        }

        holder.itemBinding.rvDealsImage.setOnClickListener(v -> listener.setSelected(position, currentProduct.getUid()));

        holder.itemBinding.txtDealsTitle.setOnClickListener(v -> listener.setSelected(position, currentProduct.getUid()));

    }

    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ProductItemBinding itemBinding;

        public ViewHolder(ProductItemBinding view) {
            super(view.getRoot());
            itemBinding = view;
        }
    }
}
