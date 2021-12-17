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
import com.ruzz.butilordering.Model.DealsModel;
import com.ruzz.butilordering.Model.OrderModel;
import com.ruzz.butilordering.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.viewHolder> {
    private Context ctx;
    private List<OrderModel> orderModelList;

    public OrderAdapter(Context ctx, List<OrderModel> orderModelList) {
        this.ctx = ctx;
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public OrderAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(ctx).inflate(R.layout.item_my_orders,parent, false);
        return new OrderAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.viewHolder holder, int position) {
        final OrderModel orderModel=orderModelList.get(position);
        //0 from order, 1 for delivered, 2 for cancelled
        if (orderModel.getOrderStatus().equals("0")) {
            holder.orderStatus.setText("Order on "+orderModel.getStatusDate());
        }else if (orderModel.getOrderStatus().equals("1")) {
            holder.orderStatus.setText("Delivered on "+orderModel.getStatusDate());
        }else if (orderModel.getOrderStatus().equals("2")) {
            holder.orderStatus.setText("Cancelled on "+orderModel.getStatusDate());
        }
        holder.orderBrand.setText(orderModel.getOrderBrand());
        Glide.with(ctx).load(orderModel.getImage()).into(holder.orderImage);

    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private ImageView orderImage;
        private TextView orderStatus,orderBrand;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.iv_orderImage);
            orderStatus = itemView.findViewById(R.id.iv_orderStatus);
            orderBrand = itemView.findViewById(R.id.iv_orderBrand);
        }
    }
}
