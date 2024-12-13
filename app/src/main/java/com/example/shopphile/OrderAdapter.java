package com.example.shopphile;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_recyclerview, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderNumber.setText("#" + String.format("%010d", order.getOrderId()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = dateFormat.parse(order.getOrderDate());
            SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
            holder.orderDate.setText("Placed on " + displayFormat.format(date));
        } catch (Exception e) {
            holder.orderDate.setText("Placed on " + order.getOrderDate());
        }

        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(order.getOrderItems());
        holder.orderItemsRecyclerView.setAdapter(orderItemAdapter);
        holder.orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(holder.orderItemsRecyclerView.getContext()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumber;
        TextView orderDate;
        RecyclerView orderItemsRecyclerView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumber = itemView.findViewById(R.id.order_number);
            orderDate = itemView.findViewById(R.id.order_date);
            orderItemsRecyclerView = itemView.findViewById(R.id.order_items_recycler_view);
        }
    }
}
