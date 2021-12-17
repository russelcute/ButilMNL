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
import com.ruzz.butilordering.Model.CategoryModel;
import com.ruzz.butilordering.Model.OfferModel;
import com.ruzz.butilordering.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {
    private Context ctx;
    private List<CategoryModel> categoryModel;

    public CategoryAdapter(Context ctx, List<CategoryModel> categoryModel) {
        this.ctx = ctx;
        this.categoryModel = categoryModel;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(ctx).inflate(R.layout.item_categories,parent, false);
        return new CategoryAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewHolder holder, int position) {
        final CategoryModel categoryModel1=categoryModel.get(position);
        holder.categoryTitle.setText(categoryModel1.getCategoryTitle());
        Glide.with(ctx).load(categoryModel1.getCategoryImage()).into(holder.categoryImage);
    }

    @Override
    public int getItemCount() {
        return categoryModel.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImage;
        private TextView categoryTitle;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage=itemView.findViewById(R.id.rv_categories);
            categoryTitle=itemView.findViewById(R.id.txt_categoryTitle);
        }
    }
}
