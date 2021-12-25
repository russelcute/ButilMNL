package com.ruzz.butilordering.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.databinding.ItemOrderBinding;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<OrderModel> userOrders;
    private OrderSelected listener;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public OrderAdapter (OrderSelected listener, List<OrderModel> orders) {
        this.userOrders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderAdapter.ViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel currentOrder = userOrders.get(position);

        if (currentOrder.isDelivered()) {
            holder.itemBinding.imgCheck.setVisibility(View.VISIBLE);
            holder.itemBinding.btnLocateOrder.setVisibility(View.GONE);
        } else {
            holder.itemBinding.imgCheck.setVisibility(View.GONE);
            holder.itemBinding.btnLocateOrder.setVisibility(View.VISIBLE);
        }

        if (currentOrder.isPaid()) {
            holder.itemBinding.txtOrderPaid.setText("paid");
        } else {
            holder.itemBinding.txtOrderPaid.setText("unpaid");
        }

        String[] date = currentOrder.getOrderDate().toString().split(" ", 5);

        holder.itemBinding.txtOrderDate.setText(date[0] + " " + date[1] + " " + date[2] + " " + date[3]);
        holder.itemBinding.txtOrderId.setText(currentOrder.getUserid());
        String price = "₱" + df.format(currentOrder.getAmountDue());
        holder.itemBinding.txtAmountDue.setText(price);

        holder.itemBinding.circleImageView2.setOnClickListener(v -> {
            listener.selectedOrder(currentOrder.getUserid(), "OrderInfo");
        });

        holder.itemBinding.btnLocateOrder.setOnClickListener(v -> {
            if (currentOrder.getLatitude() != 0 && currentOrder.getLongitude() != 0) {
                listener.selectedOrder(currentOrder.getUserid(), "OrderLocation");
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.userOrders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding itemBinding;

        public ViewHolder(ItemOrderBinding view) {
            super(view.getRoot());
            itemBinding = view;
        }
    }
}
