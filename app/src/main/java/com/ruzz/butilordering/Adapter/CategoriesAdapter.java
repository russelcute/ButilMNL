package com.ruzz.butilordering.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruzz.butilordering.Model.CategoriesModel;
import com.ruzz.butilordering.databinding.ItemCategoryBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private List<CategoriesModel> categories;
    private CategoriesInterface listener;

    public CategoriesAdapter (CategoriesInterface listener, List<CategoriesModel> categories) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoriesAdapter.ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoriesModel category = categories.get(position);

        holder.itemBinding.txtCategoryTitle.setText(category.getTitle());
        ImageView cirlceImage = (ImageView) holder.itemBinding.circleImageView3;
        Picasso.get().load(category.getImage()).into(cirlceImage);

        holder.itemBinding.circleImageView3.setOnClickListener(v -> {
            listener.changeCategory(category.getTitle());
        });

    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCategoryBinding itemBinding;

        public ViewHolder(ItemCategoryBinding view) {
            super(view.getRoot());
            itemBinding = view;
        }
    }

}
