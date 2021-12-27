package com.ruzz.butilordering.Adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruzz.butilordering.HomeActivity;
import com.ruzz.butilordering.Model.ProductModel;
import com.ruzz.butilordering.databinding.ProductItemBinding;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<ProductModel> productList;
    private ProductSelected listener;
    private List<String> likedProducts;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public ProductsAdapter(ProductSelected listener, List<ProductModel> products, List<String> liked) {
        this.productList = products;
        this.listener = listener;
        this.likedProducts = liked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ProductItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        ProductModel currentProduct = productList.get(position);
        boolean liked = false;

        for (String product : likedProducts) {
            if (product.equals(currentProduct.getUid())) {
                liked = true;
            }
        }

        String title = currentProduct.getName() + " " + "(" + (int) currentProduct.getContent() + "g" + ")";
        holder.itemBinding.txtDealsTitle.setText(title);
        if (currentProduct.getType() != null && !currentProduct.getType().equals("Standard")) {
            String type = currentProduct.getType() + " " + currentProduct.getCategoryId();
            holder.itemBinding.txtDealsType.setText(type);
        } else {
            holder.itemBinding.txtDealsType.setText(currentProduct.getCategoryId());
        }

        String price = "₱" + df.format(currentProduct.getPrice());
        if (currentProduct.getPromo() > 1) {
            double discountedPrice = currentProduct.getPrice() - (currentProduct.getPrice() * (currentProduct.getPromo() / 100));
            String priceDiscount = "₱" + df.format(discountedPrice);
            TextView priceView = holder.itemBinding.txtDealsPrice;
            priceView.setText(price);
            priceView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.itemBinding.txtDealsPromo.setVisibility(View.VISIBLE);
            holder.itemBinding.txtDealsPromo.setText(priceDiscount);
        } else {
            holder.itemBinding.txtDealsPromo.setVisibility(View.GONE);
            holder.itemBinding.txtDealsPrice.setText(price);
        }

        ImageView productImage = holder.itemBinding.rvDealsImage;
        Picasso.get().load(currentProduct.getImage()).into(productImage);

        if (currentProduct.getPromo() == 0) {
            holder.itemBinding.txtDealsDiscountedPercentage.setVisibility(View.GONE);
        } else {
            String promo = "—" + (int) currentProduct.getPromo() + "%";
            holder.itemBinding.txtDealsDiscountedPercentage.setText(promo);
        }


        holder.itemBinding.imgLike.setVisibility(liked ? View.GONE : View.VISIBLE);
        holder.itemBinding.imgFavorite.setVisibility(liked ? View.VISIBLE : View.GONE);


        boolean finalLiked = liked;
        holder.itemBinding.rvDealsImage.setOnClickListener(v -> listener.setSelected(position, currentProduct.getUid(), finalLiked));

        boolean finalLiked1 = liked;
        holder.itemBinding.txtDealsTitle.setOnClickListener(v -> listener.setSelected(position, currentProduct.getUid(), finalLiked1));

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
