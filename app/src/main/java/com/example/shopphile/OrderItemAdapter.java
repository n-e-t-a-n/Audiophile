package com.example.shopphile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private List<OrderItem> orderItemList;

    public OrderItemAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_items, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);

        holder.productName.setText(orderItem.getProductName());
        holder.brandName.setText(orderItem.getBrandName());
        holder.productPrice.setText(String.format("$%.2f", orderItem.getProductPrice()));

        int imageResource = holder.itemView.getContext().getResources()
                .getIdentifier(orderItem.getProductImage(), "drawable", holder.itemView.getContext().getPackageName());

        if (imageResource != 0) {
            holder.productImage.setImageResource(imageResource);
        } else {
            holder.productImage.setImageResource(R.drawable.flint_v2);
        }

        holder.orderQuantity.setText(orderItem.getQuantity() + "x");
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView brandName, productName, productPrice, orderQuantity;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            brandName = itemView.findViewById(R.id.brand_name);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            orderQuantity = itemView.findViewById(R.id.order_quantity);
        }
    }
}
