package com.example.shopphile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartUpdateListener {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private TextView totalCount, totalAmount, shippingAmount, payableAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        // Initialize views
        recyclerViewCart = findViewById(R.id.recycler_view_cart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        totalCount = findViewById(R.id.total_count);
        totalAmount = findViewById(R.id.total_amount);
        shippingAmount = findViewById(R.id.shipping_amount);
        payableAmount = findViewById(R.id.payable_amount);

        cartItems = null;
        cartAdapter = new CartAdapter(cartItems, this, this);
        recyclerViewCart.setAdapter(cartAdapter);

        calculateTotals();

        // BACK BUTTON
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // CHECKOUt
        Button checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(v -> {
            checkout();
            Intent intent = new Intent(CartActivity.this, OrdersActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onCartUpdated() {
        calculateTotals();
    }

    private void checkout() {

    }

    void calculateTotals() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProductPrice() * item.getQuantity();
        }
        totalCount.setText(String.valueOf(cartItems.size()));
        totalAmount.setText(String.format("$%.2f", total));
        double shipping = cartItems.isEmpty() ? 0 : 30;
        shippingAmount.setText(String.format("$%.2f", shipping));
        double payable = total + shipping; // Total + Shipping
        payableAmount.setText(String.format("$%.2f", payable));
    }
}
