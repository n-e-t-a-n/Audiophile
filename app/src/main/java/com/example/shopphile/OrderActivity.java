package com.example.shopphile;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private ArrayList<OrderItem> orderItems;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_view_order_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOrderHistory();
    }

    private void loadOrderHistory() {
        String userEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        if (userEmail != null) {
            DocumentReference userDocRef = db.collection("users").document(userEmail);

            userDocRef.get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(OrderActivity.this, "Error getting order history: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentSnapshot document = task.getResult();

                if (document == null || !document.exists()) {
                    Toast.makeText(OrderActivity.this, "No orders found", Toast.LENGTH_SHORT).show();
                    return;
                }

                @SuppressWarnings("unchecked")
                List<OrderItem> orderItemsList = (List<OrderItem>) document.get("orders");

                if (orderItemsList == null) {
                    Toast.makeText(OrderActivity.this, "You have no order history", Toast.LENGTH_SHORT).show();
                    return;
                }

                orderItems = new ArrayList<>();

                for (Object obj : orderItemsList) {
                    if (obj instanceof HashMap) {
                        @SuppressWarnings("unchecked")
                        HashMap<String, Object> itemMap = (HashMap<String, Object>) obj;

                        String productName = (String) itemMap.getOrDefault("productName", "Unknown Product");
                        String productSeller = (String) itemMap.getOrDefault("productSeller", "Unknown Seller");
                        double productPrice = itemMap.containsKey("productPrice") && itemMap.get("productPrice") != null
                                ? ((Number) Objects.requireNonNull(itemMap.get("productPrice"))).doubleValue()
                                : 0.0;
                        String productImage = (String) itemMap.getOrDefault("productImage", "");
                        String orderDate = (String) itemMap.getOrDefault("orderDate", "N/A");

                        OrderItem orderItem = new OrderItem(
                                productImage,
                                productSeller,
                                productName,
                                productPrice,
                                orderDate
                        );

                        orderItems.add(orderItem);
                    }
                }

                orderAdapter = new OrderAdapter(this, orderItems);
                recyclerView.setAdapter(orderAdapter);
            });
        }
    }
}
