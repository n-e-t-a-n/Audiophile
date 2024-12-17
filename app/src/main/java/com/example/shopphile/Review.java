package com.example.shopphile;

public class Review {
    private final int rating;
    private final String review;
    private final String email;

    public Review(int rating, String review, String email) {
        this.rating = rating;
        this.review = review;
        this.email = email;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public String getEmail() {
        return email;
    }
}
