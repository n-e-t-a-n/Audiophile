package com.example.shopphile;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private final List<Review> reviews;

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review, parent, false);
        return new ReviewViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < review.getRating(); i++) {
            stars.append("★");
        }
        holder.reviewRating.setText(stars.toString());

        holder.reviewText.setText(review.getReview());
        holder.reviewUser.setText("By: " + review.getEmail());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewRating, reviewText, reviewUser;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewRating = itemView.findViewById(R.id.review_rating);
            reviewText = itemView.findViewById(R.id.review_text);
            reviewUser = itemView.findViewById(R.id.review_user);
        }
    }
}
