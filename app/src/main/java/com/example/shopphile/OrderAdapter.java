package com.example.shopphile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final Context context;
    private final List<OrderItem> orderItems;

    public OrderAdapter(Context context, List<OrderItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_items, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);

        holder.productName.setText(item.getProductName());
        holder.productSeller.setText(item.getBrandName());
        holder.productPrice.setText(String.format("$%.2f", item.getProductPrice()));
        holder.orderDate.setText(item.getOrderDate());

        Glide.with(context)
                .clear(holder.productImage);
        Glide.with(context)
                .load(item.getProductImage())
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productSeller, productPrice, orderDate;
        ImageView productImage;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_name);
            productSeller = itemView.findViewById(R.id.product_seller);
            productPrice = itemView.findViewById(R.id.product_price);
            orderDate = itemView.findViewById(R.id.order_date);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}
