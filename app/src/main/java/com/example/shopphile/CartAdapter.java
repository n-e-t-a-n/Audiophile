package com.example.shopphile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private Context context;
    private OnCartUpdateListener listener;
    private DBHelper dbHelper;

    public interface OnCartUpdateListener {
        void onCartUpdated();
    }

    public CartAdapter(List<CartItem> cartItems, Context context, OnCartUpdateListener listener) {
        this.cartItems = cartItems;
        this.context = context;
        this.listener = listener;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_items, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.productName.setText(item.getProductName());
        holder.brandName.setText(item.getBrandName());
        holder.productPrice.setText(String.format("$%.2f", item.getProductPrice()));
        holder.cartItemQuantity.setText(String.valueOf(item.getQuantity()));
        holder.productImage.setImageResource(item.getProductImage());

        // ADD QUANTITY
        holder.addQuantity.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            listener.onCartUpdated();
        });

        // SUBTRACT QUANTITY
        holder.minusQuantity.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                listener.onCartUpdated();
            } else {
                dbHelper.deleteCartItem(item.getProductName());
                cartItems.remove(position);
                notifyItemRemoved(position);
                listener.onCartUpdated();
            }
        });

        // DELETE ITEM
        holder.deleteItem.setOnClickListener(v -> {
            dbHelper.deleteCartItem(item.getProductName());
            cartItems.remove(position);
            notifyItemRemoved(position);
            listener.onCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, brandName, productPrice, cartItemQuantity;
        ImageView addQuantity, minusQuantity, deleteItem, productImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            brandName = itemView.findViewById(R.id.brand_name);
            productPrice = itemView.findViewById(R.id.product_price);
            cartItemQuantity = itemView.findViewById(R.id.cart_item_count);
            addQuantity = itemView.findViewById(R.id.add_quantity);
            minusQuantity = itemView.findViewById(R.id.minus_quantity);
            deleteItem = itemView.findViewById(R.id.delete_item);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}
