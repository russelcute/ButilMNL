package com.ruzz.butilordering.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruzz.butilordering.Model.SliderData;
import com.ruzz.butilordering.Model.SliderModel;
import com.ruzz.butilordering.R;
import com.ruzz.butilordering.databinding.ItemCategoryBinding;
import com.ruzz.butilordering.databinding.ItemImageScrollerBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.viewHolder> {
    private List<SliderData> sliderImages;

    public SliderAdapter(List<SliderData> images) {
        this.sliderImages = images;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return new SliderAdapter.viewHolder(ItemImageScrollerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.viewHolder holder, int position) {
        SliderData currentImage = sliderImages.get(position);

        ImageView image = holder.sliderImage.imageCurrent;
        Picasso.get().load(currentImage.getImgUrl()).into(image);
    }

    @Override
    public int getCount() {
        return sliderImages.size();
    }

    public class viewHolder extends SliderViewAdapter.ViewHolder {
        ItemImageScrollerBinding sliderImage;
        public viewHolder(@NonNull ItemImageScrollerBinding itemView) {
            super(itemView.getRoot());

            sliderImage = itemView;
        }
    }
}
