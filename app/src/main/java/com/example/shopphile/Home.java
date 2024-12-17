package com.example.shopphile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private RecyclerView recyclerViewPopularProducts;
    private ProductAdapter adapter;
    private List<CartItem> popularProducts;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(Home.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.home);
        recyclerViewPopularProducts = findViewById(R.id.recycler_view_popular_products);

        EditText searchBarHome = findViewById(R.id.searchBarHome);

        searchBarHome.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, ShopActivity.class);
            intent.putExtra("focusSearch", true);
            startActivity(intent);
        });

        setupRecyclerView();
        setupClickListeners();
    }

    private void setupRecyclerView() {
        recyclerViewPopularProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
                            String description = document.getString("description");
                            String category = document.getString("category");

                            if (name != null && seller != null && imageUrl != null && stock != null && price != null && description != null && category != null) {
                                popularProducts.add(new CartItem(name, seller, price, Math.toIntExact(stock), imageUrl, description, category));
                            }
                        }

                        adapter = new ProductAdapter(popularProducts, this);
                        recyclerViewPopularProducts.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(e ->
                    Log.w("Firestore", "Error getting documents.", e)
                );
    }

    private void setupClickListeners() {
        ImageView cartButton = findViewById(R.id.cart_button);
        cartButton.setOnClickListener(v -> startActivity(new Intent(Home.this, CartActivity.class)));

        View.OnClickListener goToOrders = v -> startActivity(new Intent(Home.this, OrderActivity.class));

        Button shopNowButton = findViewById(R.id.shopnow_button);
        shopNowButton.setOnClickListener(v -> openShopActivity());

        ImageView shopButton = findViewById(R.id.shop_button);
        shopButton.setOnClickListener(v -> openShopActivity());

        TextView shopText = findViewById(R.id.shop_text);
        shopText.setOnClickListener(v -> openShopActivity());

        ImageView ordersButton = findViewById(R.id.orders_button);
        ordersButton.setOnClickListener(goToOrders);

        TextView ordersButtonText = findViewById(R.id.orders_button_text);
        ordersButtonText.setOnClickListener(goToOrders);

        ImageView signOutButton = findViewById(R.id.signout);
        TextView signOutText = findViewById(R.id.signout_text);

        signOutButton.setOnClickListener(v -> openProfileActivity());
        signOutText.setOnClickListener(v -> openProfileActivity());
    }

    private void openShopActivity() {
        Intent intent = new Intent(Home.this, ShopActivity.class);
        startActivity(intent);
    }

    private void openProfileActivity() {
        Intent intent = new Intent(Home.this, ProfileActivity.class);
        startActivity(intent);
    }
}
