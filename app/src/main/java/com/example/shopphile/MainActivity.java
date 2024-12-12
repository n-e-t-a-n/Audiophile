package com.example.shopphile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPopularProducts;
    private ProductAdapter adapter;
    private List<CartItem> popularProducts;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is logged in
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // No user is logged in, redirect to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Finish the current activity to prevent back navigation
            return;
        }

        // Proceed if user is logged in
        setContentView(R.layout.home);

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initializeViews() {
        recyclerViewPopularProducts = findViewById(R.id.recycler_view_popular_products);
    }

    private void setupRecyclerView() {
        recyclerViewPopularProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // SAMPLE PRODUCTS
        popularProducts = new ArrayList<>();
        popularProducts.add(new CartItem("Longsword", "EAE", 249.00, 1, R.drawable.longsword));
        popularProducts.add(new CartItem("Flint V2", "Strymon", 349.00, 1, R.drawable.flint_v2));
        popularProducts.add(new CartItem("ACS1", "Walrus", 296.34, 1, R.drawable.acs1));
        popularProducts.add(new CartItem("DD-8", "Boss", 179.99, 1, R.drawable.dd_8));
        popularProducts.add(new CartItem("Morning Glory V4", "JHS", 199.00, 1, R.drawable.morning_glory));

        adapter = new ProductAdapter(popularProducts, this);
        recyclerViewPopularProducts.setAdapter(adapter);
    }

    private void setupClickListeners() {
        // Cart button navigation
        ImageView cartButton = findViewById(R.id.cart_button);
        cartButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));

        // Orders button navigation (same logic for both buttons)
        View.OnClickListener goToOrders = v -> startActivity(new Intent(MainActivity.this, OrdersActivity.class));

        ImageView ordersButton = findViewById(R.id.orders_button);
        ordersButton.setOnClickListener(goToOrders);

        TextView ordersButtonText = findViewById(R.id.orders_button_text);
        ordersButtonText.setOnClickListener(goToOrders);

        // Sign out button navigation
        ImageView signOutButton = findViewById(R.id.signout);  // ImageView for sign out
        TextView signOutText = findViewById(R.id.signout_text); // TextView for sign out

        // Set the same OnClickListener for both sign out button and sign out text
        View.OnClickListener signOutListener = v -> {
            mAuth.signOut();  // Sign out from Firebase Auth
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);  // Redirect to login
            startActivity(intent);
            finish();  // Close the current activity
        };

        signOutButton.setOnClickListener(signOutListener);
        signOutText.setOnClickListener(signOutListener);
    }
}
