package com.example.shopphile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class OrderActivity extends AppCompatActivity implements OrderAdapter.OnItemClickListener {
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

                        String orderDate;
                        if (itemMap.containsKey("orderDate") && itemMap.get("orderDate") instanceof Timestamp) {
                            Timestamp timestamp = (Timestamp) itemMap.get("orderDate");

                            assert timestamp != null;
                            Date date = timestamp.toDate();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            orderDate = sdf.format(date);
                        } else {
                            orderDate = "N/A";
                        }

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

                Collections.reverse(orderItems);

                orderAdapter = new OrderAdapter(this, orderItems, this);
                recyclerView.setAdapter(orderAdapter);
            });
        }
    }

    @Override
    public void onItemClick(OrderItem item) {
        android.view.LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.rating_dialog, null);

        RatingBar ratingBar = dialogView.findViewById(R.id.rating_bar);
        EditText reviewEditText = dialogView.findViewById(R.id.edit_text_review);

        String currentUserEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        assert currentUserEmail != null;

        DocumentReference userRef = db.collection("users").document(currentUserEmail);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> reviews = (List<Map<String, Object>>) documentSnapshot.get("reviews");

                if (reviews != null) {
                    for (Map<String, Object> review : reviews) {
                        if (Objects.equals(review.get("productName"), item.getProductName())) {
                            float existingRating = review.containsKey("rating") ? Float.parseFloat(String.valueOf(review.get("rating"))) : 0;
                            String existingReview = review.containsKey("review") ? (String) review.get("review") : "";

                            if (existingRating > 0) {
                                ratingBar.setRating(existingRating);
                                ratingBar.setIsIndicator(true);
                            }

                            assert existingReview != null;
                            if (!existingReview.isEmpty()) {
                                reviewEditText.setText(existingReview);
                                reviewEditText.setEnabled(true);
                            }
                            break;
                        }
                    }
                }
            }
        });

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Review " + item.getProductName());
        builder.setView(dialogView);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            float rating = ratingBar.getRating();
            String reviewText = reviewEditText.getText().toString().trim();

            if (rating == 0 || reviewText.isEmpty()) {
                Toast.makeText(this, "Please provide a rating and review.", Toast.LENGTH_SHORT).show();
                return;
            }

            updateUserReview(item, rating, reviewText);
            updateItemReview(item, rating, reviewText);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void updateUserReview(OrderItem item, float rating, String reviewText) {
        String currentUserEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        assert currentUserEmail != null;

        db.collection("users")
                .document(currentUserEmail)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> reviews = (List<Map<String, Object>>) documentSnapshot.get("reviews");
                        boolean updated = false;

                        if (reviews != null) {
                            for (Map<String, Object> review : reviews) {
                                if (Objects.equals(review.get("productName"), item.getProductName())) {
                                    review.put("rating", (int) rating);
                                    review.put("review", reviewText);
                                    updated = true;
                                    break;
                                }
                            }
                        } else {
                            reviews = new ArrayList<>();
                        }

                        if (!updated) {
                            Map<String, Object> newReview = new HashMap<>();
                            newReview.put("productName", item.getProductName());
                            newReview.put("rating", (int) rating);
                            newReview.put("review", reviewText);
                            reviews.add(newReview);
                        }

                        db.collection("users")
                                .document(currentUserEmail)
                                .update("reviews", reviews)
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "User review updated!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update user review: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
    }

    private void updateItemReview(OrderItem item, float rating, String reviewText) {
        db.collection("items")
                .whereEqualTo("name", item.getProductName())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        DocumentReference itemRef = document.getReference();

                        Long ratingAmount = document.contains("ratingAmount")
                                ? document.getLong("ratingAmount")
                                : Long.valueOf(0L);

                        Long totalRating = document.contains("totalRating")
                                ? document.getLong("totalRating")
                                : Long.valueOf(0L);

                        ratingAmount = (ratingAmount != null) ? ratingAmount + 1 : 1;
                        totalRating = (totalRating != null) ? totalRating + (int) rating : (int) rating;

                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> reviews = (List<Map<String, Object>>) document.get("reviews");
                        if (reviews == null) {
                            reviews = new ArrayList<>();
                        }
                        Map<String, Object> newReview = new HashMap<>();
                        newReview.put("rating", (int) rating);
                        newReview.put("review", reviewText);
                        reviews.add(newReview);

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("ratingAmount", ratingAmount);
                        updates.put("totalRating", totalRating);
                        updates.put("reviews", reviews);

                        itemRef.update(updates)
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Item review updated!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Item not found in the database.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error querying item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
