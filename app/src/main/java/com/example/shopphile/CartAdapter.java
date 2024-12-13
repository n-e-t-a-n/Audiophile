package com.example.shopphile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final List<CartItem> cartItems;
    private final Context context;
    private final OnCartUpdateListener listener;

    public interface OnCartUpdateListener {
        void onCartUpdated();
    }

    public CartAdapter(List<CartItem> cartItems, Context context, OnCartUpdateListener listener) {
        this.cartItems = cartItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_items, parent, false);
        return new CartViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.productName.setText(item.getProductName());
        holder.brandName.setText(item.getBrandName());
        holder.productPrice.setText(String.format("$%.2f", item.getProductPrice()));
        holder.cartItemQuantity.setText(String.valueOf(item.getQuantity()));

        holder.deleteItem.setVisibility(View.VISIBLE);

        Glide.with(context)
                .clear(holder.productImage);
        Glide.with(context)
                .load(item.getProductImage())
                .into(holder.productImage);

        holder.deleteItem.setOnClickListener(v -> {
            CartItem itemToDelete = cartItems.get(position);
            cartItems.remove(position);
            notifyItemRemoved(position);
            listener.onCartUpdated();

            deleteItemFromFirestore(itemToDelete.getProductName());
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
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

    private void deleteItemFromFirestore(String productName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String userEmail = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        if (userEmail == null) return;

        DocumentReference userDocRef = db.collection("users").document(userEmail);

        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                @SuppressWarnings("unchecked")
                List<HashMap<String, Object>> cartList = (List<HashMap<String, Object>>) documentSnapshot.get("cart");

                if (cartList != null) {
                    for (int i = 0; i < cartList.size(); i++) {
                        String name = (String) cartList.get(i).get("productName");
                        if (name != null && name.equals(productName)) {
                            cartList.remove(i);
                            break;
                        }
                    }

                    userDocRef.update("cart", cartList)
                            .addOnSuccessListener(aVoid ->
                                Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show()
                            )
                            .addOnFailureListener(e ->
                                Toast.makeText(context, "Failed to update cart: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                }
            }
        }).addOnFailureListener(e ->
            Toast.makeText(context, "Error fetching cart: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

}
