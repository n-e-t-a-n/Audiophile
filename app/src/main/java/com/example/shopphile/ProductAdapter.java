package com.example.shopphile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<CartItem> products;
    private DBHelper dbHelper;
    private Context context;

    public ProductAdapter(List<CartItem> products, Context context) {
        this.products = products;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        CartItem item = products.get(position);

        holder.productName.setText(item.getProductName());
        holder.brandName.setText(item.getBrandName());
        holder.productPrice.setText("$" + item.getProductPrice());
        holder.productImage.setImageResource(item.getProductImage());

        holder.addToCartButton.setOnClickListener(v -> {
            dbHelper.addItemToCart(item.getProductName(), item.getProductPrice(), 1, item.getBrandName(), item.getProductImage());
            Toast.makeText(context, item.getProductName() + " added to cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, brandName, productPrice;
        ImageView productImage, addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            brandName = itemView.findViewById(R.id.brand_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
            addToCartButton = itemView.findViewById(R.id.addtocart_button);
        }
    }
}
