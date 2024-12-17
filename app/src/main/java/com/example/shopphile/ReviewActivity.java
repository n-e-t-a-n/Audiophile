package com.example.shopphile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    private ReviewAdapter reviewAdapter;
    private final List<Review> reviewList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_page);

        db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.reviews_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reviewAdapter = new ReviewAdapter(reviewList);
        recyclerView.setAdapter(reviewAdapter);

        String itemName = getIntent().getStringExtra("itemName");

        loadReviews(itemName);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadReviews(String itemName) {
        db.collection("items")
                .whereEqualTo("name", itemName)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        @SuppressWarnings("unchecked")
                        List<Review> reviews = (List<Review>) querySnapshot.getDocuments().get(0).get("reviews");

                        if (reviews != null) {
                            for (Object obj : reviews) {
                                if (obj instanceof java.util.HashMap) {
                                    @SuppressWarnings("unchecked")
                                    java.util.Map<String, Object> map = (java.util.HashMap<String, Object>) obj;

                                    Object ratingObj = map.get("rating");

                                    int rating = (ratingObj != null) ? ((Number) ratingObj).intValue() : 0;

                                    String reviewText = (String) map.get("review");
                                    String email = (String) map.get("email");

                                    reviewList.add(new Review(rating, reviewText, email));
                                }
                            }
                            reviewAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(this, "No reviews found for this item.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error fetching reviews: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
