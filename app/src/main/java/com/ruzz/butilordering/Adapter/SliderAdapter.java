package com.ruzz.butilordering.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruzz.butilordering.Model.SliderModel;
import com.ruzz.butilordering.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.viewHolder> {
    private SliderModel[] sliderModels;
    private Context ctx;

    public SliderAdapter(SliderModel[] sliderModels, Context ctx) {
        this.sliderModels = sliderModels;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        View view= LayoutInflater.from(ctx).inflate(R.layout.item_slider_layout, parent, false);
        return new SliderAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.viewHolder holder, int position) {
        final SliderModel sliderModel = sliderModels[position];
        Glide.with(ctx).load(sliderModel.getSliderImage()).into(holder.sliderImage);
    }

    @Override
    public int getCount() {
        return sliderModels.length;
    }

    public class viewHolder extends SliderViewAdapter.ViewHolder {
        private ImageView sliderImage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            sliderImage = itemView.findViewById(R.id.iv_sliderImage);
        }
    }
}
