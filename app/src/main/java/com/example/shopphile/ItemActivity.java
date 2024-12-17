package com.example.shopphile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ItemActivity extends AppCompatActivity {

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

        ImageView backButton = findViewById(R.id.back_button);
        ImageView productImageView = findViewById(R.id.product_image);
        TextView productNameTextView = findViewById(R.id.product_name);
        TextView productCategoryTextView = findViewById(R.id.product_category);
        TextView productSellerTextView = findViewById(R.id.product_seller);
        TextView productDescriptionTextView = findViewById(R.id.product_description);
        TextView productPriceTextView = findViewById(R.id.product_price);
        TextView productStockTextView = findViewById(R.id.product_stock);
        Button orderButton = findViewById(R.id.order_button);
        ImageButton readReviews = findViewById(R.id.read_reviews);

        Intent intent = getIntent();

        String productName = intent.getStringExtra("productName");
        String productCategory = intent.getStringExtra("productCategory");
        String productSeller = intent.getStringExtra("productSeller");
        String productDescription = intent.getStringExtra("productDescription");
        double productPrice = intent.getDoubleExtra("productPrice", 0.0);
        int productStock = intent.getIntExtra("productStock", 0);
        String productImage = intent.getStringExtra("productImage");

        productNameTextView.setText(productName);
        productCategoryTextView.setText("Category: " + productCategory);
        productSellerTextView.setText("Seller: " + productSeller);
        productDescriptionTextView.setText(productDescription);
        productPriceTextView.setText(String.format("$%.2f", productPrice));
        productStockTextView.setText("Stock: " + productStock);

        Glide.with(this)
                .load(productImage)
                .into(productImageView);

        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        orderButton.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                String email = currentUser.getEmail();
                assert email != null;

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                DocumentReference userDocRef = db.collection("users").document(email);

                Map<String, Object> cartItem = new HashMap<>();
                cartItem.put("productName", productName);
                cartItem.put("productCategory", productCategory);
                cartItem.put("productSeller", productSeller);
                cartItem.put("productDescription", productDescription);
                cartItem.put("productPrice", productPrice);
                cartItem.put("productStock", productStock);
                cartItem.put("productImage", productImage);

                userDocRef.update("cart", FieldValue.arrayUnion(cartItem))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                getOnBackPressedDispatcher().onBackPressed();
                                Toast.makeText(ItemActivity.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ItemActivity.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(ItemActivity.this, "Please log in to add items to your cart", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ItemActivity.this, LoginActivity.class));
            }
        });

        readReviews.setOnClickListener(v -> {
            Intent reviewIntent = new Intent(this, ReviewActivity.class);
            reviewIntent.putExtra("itemName", productName);

            startActivity(reviewIntent);
        });
    }
}
