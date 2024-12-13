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
    private List<CartItem> popularProducts;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText searchBarShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shop);

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        setupSearchBar();
    }

    private void initializeViews() {
        recyclerViewPopularProducts = findViewById(R.id.recycler_view_popular_products);
        searchBarShop = findViewById(R.id.searchBarShop);
    }

    private void setupRecyclerView() {
        recyclerViewPopularProducts.setLayoutManager(new GridLayoutManager(this, 2));
        popularProducts = new ArrayList<>();

        db.collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String name = document.getString("name");
                            String seller = document.getString("seller");
                            String imageUrl = document.getString("imageUrl");
                            Long stock = document.getLong("stock");
                            Double price = document.getDouble("price");
                            String category = document.getString("category");
                            String description = document.getString("description");

                            if (name != null && seller != null && imageUrl != null && stock != null && price != null) {
                                popularProducts.add(new CartItem(name, seller, price, Math.toIntExact(stock), imageUrl, description, category));
                            }
                        }

                        adapter = new ProductAdapter(popularProducts, this);
                        recyclerViewPopularProducts.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error getting documents.", e));
    }

    private void setupClickListeners() {
        ImageView cartButton = findViewById(R.id.cart_button);
        cartButton.setOnClickListener(v -> startActivity(new Intent(ShopActivity.this, CartActivity.class)));

        View.OnClickListener goToOrders = v -> startActivity(new Intent(ShopActivity.this, OrderActivity.class));

        ImageView ordersButton = findViewById(R.id.orders_button);
        ordersButton.setOnClickListener(goToOrders);

        TextView ordersButtonText = findViewById(R.id.orders_button_text);
        ordersButtonText.setOnClickListener(goToOrders);

        ImageView homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> startActivity(new Intent(ShopActivity.this, MainActivity.class)));

        TextView homeText = findViewById(R.id.home_text);
        homeText.setOnClickListener(v -> startActivity(new Intent(ShopActivity.this, MainActivity.class)));
    }

    private void setupSearchBar() {
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
