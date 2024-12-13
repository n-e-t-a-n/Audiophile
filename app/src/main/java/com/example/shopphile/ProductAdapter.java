package com.example.shopphile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<CartItem> productList;
    private final List<CartItem> filteredProductList;
    private final Context context;

    public ProductAdapter(List<CartItem> productList, Context context) {
        this.productList = productList;
        this.filteredProductList = new ArrayList<>(productList);
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        CartItem product = filteredProductList.get(position);

        holder.nameTextView.setText(product.getProductName());
        holder.sellerTextView.setText(product.getBrandName());
        holder.priceTextView.setText(String.format("$%.2f", product.getProductPrice()));

        Glide.with(context)
                .load(product.getProductImage())
                .into(holder.productImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Item.class);

            intent.putExtra("productName", product.getProductName());
            intent.putExtra("productCategory", product.getCategory());
            intent.putExtra("productSeller", product.getBrandName());
            intent.putExtra("productDescription", product.getDescription());
            intent.putExtra("productPrice", product.getProductPrice());
            intent.putExtra("productStock", product.getQuantity());
            intent.putExtra("productImage", product.getProductImage());

            context.startActivity(intent);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        filteredProductList.clear();

        if (query.isEmpty()) {
            filteredProductList.addAll(productList);
        } else {
            query = query.toLowerCase();
            for (CartItem product : productList) {
                if (product.getProductName().toLowerCase().contains(query)) {
                    filteredProductList.add(product);
                }
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredProductList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, sellerTextView, priceTextView;
        ImageView productImageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            sellerTextView = itemView.findViewById(R.id.brand_name);
            priceTextView = itemView.findViewById(R.id.product_price);
            productImageView = itemView.findViewById(R.id.product_image);
        }
    }
}
