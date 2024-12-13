package com.example.shopphile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPopularProducts;
    private ProductAdapter adapter;
    private ProductAdapter backup;
    private List<CartItem> popularProducts;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText searchBarShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shop);  // Assuming you have a 'shop' layout for the shop page

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        setupSearchBar();  // Initialize search functionality
    }

    private void initializeViews() {
        recyclerViewPopularProducts = findViewById(R.id.recycler_view_popular_products);
        searchBarShop = findViewById(R.id.searchBarShop);  // Initialize the SearchView
    }

    private void setupRecyclerView() {
        // Use GridLayoutManager to display 2 items per row
        recyclerViewPopularProducts.setLayoutManager(new GridLayoutManager(this, 2));

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
                            String category = document.getString("category");
                            String description = document.getString("description");

                            // Add the data to the list (populating popularProducts)
                            if (name != null && seller != null && imageUrl != null && stock != null && price != null) {
                                popularProducts.add(new CartItem(name, seller, price, Math.toIntExact(stock), imageUrl, description, category));
                            }
                        }

                        // Now that we have data, set the adapter to the RecyclerView
                        adapter = new ProductAdapter(popularProducts, this);  // Pass custom layout for shop items
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
        cartButton.setOnClickListener(v -> startActivity(new Intent(ShopActivity.this, CartActivity.class)));

        // Orders button navigation
        View.OnClickListener goToOrders = v -> startActivity(new Intent(ShopActivity.this, OrdersActivity.class));

        ImageView ordersButton = findViewById(R.id.orders_button);
        ordersButton.setOnClickListener(goToOrders);

        TextView ordersButtonText = findViewById(R.id.orders_button_text);
        ordersButtonText.setOnClickListener(goToOrders);

        // Home button navigation
        ImageView homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> startActivity(new Intent(ShopActivity.this, MainActivity.class)));

        // Home text navigation
        TextView homeText = findViewById(R.id.home_text);
        homeText.setOnClickListener(v -> startActivity(new Intent(ShopActivity.this, MainActivity.class)));
    }

    private void setupSearchBar() {
        // Example where you might call reset/filter on the adapter
        searchBarShop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString();
                adapter.filter(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

    }

    

}
