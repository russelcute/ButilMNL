package com.ruzz.butilordering.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.databinding.ItemDeliveryBinding;

import java.text.DecimalFormat;
import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {
    private List<OrderModel> allDelivery;
    private DeliverySelected listener;
    private boolean hideAdd;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public DeliveryAdapter(DeliverySelected listener, List<OrderModel> deliveries, boolean hide) {
        this.allDelivery = deliveries;
        this.listener = listener;
        this.hideAdd = hide;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DeliveryAdapter.ViewHolder(ItemDeliveryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = allDelivery.get(position);

        holder.itemBinding.btnAddDeliver.setVisibility(hideAdd ? View.GONE : View.VISIBLE);

        if (order != null) {
            holder.itemBinding.txtCustomerName.setText(order.getCustomer().getUserName());
            holder.itemBinding.txtCustomerContant.setText(order.getCustomer().getContact());

            if (order.isPaid()) {
                holder.itemBinding.txtOrderPaid.setText("paid");
            } else {
                holder.itemBinding.txtOrderPaid.setText("unpaid");
            }

            String price = "₱" + df.format(order.getAmountDue());
            holder.itemBinding.txtAmountDue.setText(price);
        }

        holder.itemBinding.btnAddDeliver.setOnClickListener(v -> {
            listener.assign(order);
        });

        holder.itemBinding.btnLocateOrder.setOnClickListener(v -> {
            listener.showMap(order);
        });

        holder.itemBinding.circleImageView2.setOnClickListener(v -> {
            listener.redirect(order.getUserid());
        });

        holder.itemBinding.txtCustomerName.setOnClickListener(v -> {
            listener.redirect(order.getUserid());
        });
    }

    @Override
    public int getItemCount() {
        return allDelivery.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemDeliveryBinding itemBinding;

        public ViewHolder(ItemDeliveryBinding view) {
            super(view.getRoot());
            itemBinding = view;
        }
    }
}
