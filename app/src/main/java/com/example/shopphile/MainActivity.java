package com.example.shopphile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPopularProducts;
    private ProductAdapter adapter;
    private List<CartItem> popularProducts;
    private FirebaseAuth mAuth;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        // Initialize an empty list for popular products
        popularProducts = new ArrayList<>();

        // Get products from Firestore
        db.collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Check if documents are retrieved successfully
                    if (queryDocumentSnapshots != null) {
                        // Loop through all the documents and retrieve the data
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Access the data for each document
                            String name = document.getString("name");
                            String seller = document.getString("seller");
                            String imageUrl = document.getString("imageUrl");
                            Long stock = document.getLong("stock");
                            Double price = document.getDouble("price");
                            String description = document.getString("description");
                            String category = document.getString("category");

                            // Add the data to the list (populating popularProducts)
                            if (name != null && seller != null && imageUrl != null && stock != null && price != null && description != null && category != null) {
                                popularProducts.add(new CartItem(name, seller, price, Math.toIntExact(stock), imageUrl, description, category));
                            }
                        }

                        // Now that we have data, set the adapter to the RecyclerView
                        adapter = new ProductAdapter(popularProducts, this);
                        recyclerViewPopularProducts.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors that occur
                    Log.w("Firestore", "Error getting documents.", e);
                });
    }

    private void setupClickListeners() {
        // Cart button navigation
        ImageView cartButton = findViewById(R.id.cart_button);
        cartButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));

        // Orders button navigation (same logic for both buttons)
        View.OnClickListener goToOrders = v -> startActivity(new Intent(MainActivity.this, OrdersActivity.class));

        // Shop now button navigation
        Button shopNowButton = findViewById(R.id.shopnow_button);
        shopNowButton.setOnClickListener(v -> openShopActivity());

        // Shop button navigation
        ImageView shopButton = findViewById(R.id.shop_button);
        shopButton.setOnClickListener(v -> openShopActivity());

        // Shop text navigation
        TextView shopText = findViewById(R.id.shop_text);
        shopText.setOnClickListener(v -> openShopActivity());

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

        signOutButton.setOnClickListener(v -> openProfileActivity());
        signOutText.setOnClickListener(v -> openProfileActivity());
    }

    private void openShopActivity() {
        Intent intent = new Intent(MainActivity.this, ShopActivity.class);
        startActivity(intent);
    }

    private void openProfileActivity() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
